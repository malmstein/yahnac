package com.malmstein.yahnac.data;

import android.content.ContentValues;
import android.util.Pair;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.StoriesJsoup;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.tasks.FetchCommentsTask;
import com.malmstein.yahnac.tasks.FetchStoriesTask;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HNewsApi {

    public Observable<List<ContentValues>> getStories(final Story.TYPE type) {

        return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
            @Override
            public void call(final Subscriber<? super DataSnapshot> subscriber) {
                Firebase topStories = getStoryFirebase(type);
                topStories.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            subscriber.onNext(dataSnapshot);
                        } else {
                            Inject.crashAnalytics().logSomethingWentWrong("HNewsApi: getStories is empty for " + type.name());
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d(firebaseError.getCode());
                    }
                });
            }
        }).flatMap(new Func1<DataSnapshot, Observable<Pair<Integer, Long>>>() {
            @Override
            public Observable<Pair<Integer, Long>> call(final DataSnapshot dataSnapshot) {
                return Observable.create(new Observable.OnSubscribe<Pair<Integer, Long>>() {
                    @Override
                    public void call(Subscriber<? super Pair<Integer, Long>> subscriber) {
                        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                            Long id = (Long) dataSnapshot.child(String.valueOf(i)).getValue();
                            Integer rank = Integer.valueOf(dataSnapshot.child(String.valueOf(i)).getKey());
                            Pair<Integer, Long> storyRoot = new Pair<>(rank, id);
                            subscriber.onNext(storyRoot);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).flatMap(new Func1<Pair<Integer, Long>, Observable<ContentValues>>() {
            @Override
            public Observable<ContentValues> call(final Pair<Integer, Long> storyRoot) {
                return Observable.create(new Observable.OnSubscribe<ContentValues>() {
                    @Override
                    public void call(final Subscriber<? super ContentValues> subscriber) {
                        final Firebase story = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + storyRoot.second);
                        story.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();
                                if (newItem != null) {
                                    subscriber.onNext(mapStory(newItem, type, storyRoot.first));
                                } else {
                                    Inject.crashAnalytics().logSomethingWentWrong("HNewsApi: onDataChange is empty in " + storyRoot.second );
                                }
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                Log.d(firebaseError.getCode());
                            }
                        });
                    }
                });
            }
        }).toList();
    }

    private ContentValues mapStory(Map<String, Object> map, Story.TYPE rootType, Integer rank) {

        String by = (String) map.get("by");
        Long id = (Long) map.get("id");
        String type;
        if (rootType == Story.TYPE.best_story || rootType == Story.TYPE.new_story){
            type = Story.TYPE.top_story.name();
        } else {
            type = rootType.name();
        }
        Long time = (Long) map.get("time");
        Long score = (Long) map.get("score");
        String title = (String) map.get("title");
        String url = (String) map.get("url");
        Long descendants = (Long) map.get("descendants");

        ContentValues storyValues = new ContentValues();

        storyValues.put(HNewsContract.StoryEntry.ITEM_ID, id);
        storyValues.put(HNewsContract.StoryEntry.BY, by);
        storyValues.put(HNewsContract.StoryEntry.TYPE, type);
        storyValues.put(HNewsContract.StoryEntry.TIME_AGO, time * 1000);
        storyValues.put(HNewsContract.StoryEntry.SCORE, score);
        storyValues.put(HNewsContract.StoryEntry.TITLE, title);
        storyValues.put(HNewsContract.StoryEntry.COMMENTS, descendants);
        storyValues.put(HNewsContract.StoryEntry.URL, url);
        storyValues.put(HNewsContract.StoryEntry.RANK, rank);
        storyValues.put(HNewsContract.StoryEntry.TIMESTAMP, System.currentTimeMillis());

        return storyValues;
    }

    private Firebase getStoryFirebase(Story.TYPE type){
        switch (type){
            case top_story:
                return new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
            case new_story:
                return new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
            case best_story:
                return new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
            case show:
                return new Firebase("https://hacker-news.firebaseio.com/v0/showstories");
            case ask:
                return new Firebase("https://hacker-news.firebaseio.com/v0/askstories");
            case jobs:
                return new Firebase("https://hacker-news.firebaseio.com/v0/jobstories");
            default:
                return new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
        }
    }

    Observable<StoriesJsoup> getStories(Story.TYPE storyType, String nextUrl) {
        return Observable.create(
                new StoriesUpdateOnSubscribe(storyType, nextUrl))
                .subscribeOn(Schedulers.io());
    }

    private static class StoriesUpdateOnSubscribe implements Observable.OnSubscribe<StoriesJsoup> {

        private final Story.TYPE type;
        private final String nextUrl;
        private Subscriber<? super StoriesJsoup> subscriber;

        private StoriesUpdateOnSubscribe(Story.TYPE type, String nextUrl) {
            this.type = type;
            this.nextUrl = nextUrl;
        }

        @Override
        public void call(Subscriber<? super StoriesJsoup> subscriber) {
            this.subscriber = subscriber;
            startFetchingStories();
            subscriber.onCompleted();
        }

        private void startFetchingStories() {
            StoriesJsoup stories = StoriesJsoup.empty();
            try {
                stories = new FetchStoriesTask(type, nextUrl).execute();
            } catch (IOException e) {
                subscriber.onError(e);
            }

            if (stories.getStories().size() == 0) {
                subscriber.onError(new RuntimeException("API is not returning any data"));
            } else {
                subscriber.onNext(stories);
            }
        }

    }

    Observable<Vector<ContentValues>> getCommentsFromStory(Long storyId) {
        return Observable.create(
                new CommentsUpdateOnSubscribe(storyId))
                .subscribeOn(Schedulers.io());
    }

    private static class CommentsUpdateOnSubscribe implements Observable.OnSubscribe<Vector<ContentValues>> {

        private final Long storyId;
        private Subscriber<? super Vector<ContentValues>> subscriber;

        private CommentsUpdateOnSubscribe(Long storyId) {
            this.storyId = storyId;
        }

        @Override
        public void call(Subscriber<? super Vector<ContentValues>> subscriber) {
            this.subscriber = subscriber;
            startFetchingComments();
            subscriber.onCompleted();
        }

        private void startFetchingComments() {
            Vector<ContentValues> commentsList = new Vector<>();
            try {
                commentsList = new FetchCommentsTask(storyId).execute();
            } catch (IOException e) {
                subscriber.onError(e);
            }
            subscriber.onNext(commentsList);
        }
    }

}
