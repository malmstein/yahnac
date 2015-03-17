package com.malmstein.yahnac.data;

import android.content.ContentValues;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.malmstein.yahnac.model.StoriesJsoup;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.tasks.FetchCommentsTask;
import com.malmstein.yahnac.tasks.FetchStoriesTask;
import com.malmstein.yahnac.updater.RefreshSharedPreferences;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class HNewsApi {

    public void getStories(final Story.TYPE type, final DataPersister persister, final RefreshSharedPreferences preferences) {

        Observable<List<ContentValues>> listObservable = Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
            @Override
            public void call(final Subscriber<? super DataSnapshot> subscriber) {
                Firebase ref = new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subscriber.onNext(dataSnapshot);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }).flatMap(new Func1<DataSnapshot, Observable<Long>>() {
            @Override
            public Observable<Long> call(final DataSnapshot dataSnapshot) {
                return Observable.create(new Observable.OnSubscribe<Long>() {
                    @Override
                    public void call(Subscriber<? super Long> subscriber) {
                        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                            Long id = (Long) dataSnapshot.child(String.valueOf(i)).getValue();
                            subscriber.onNext(id);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).flatMap(new Func1<Long, Observable<ContentValues>>() {
            @Override
            public Observable<ContentValues> call(final Long id) {
                return Observable.create(new Observable.OnSubscribe<ContentValues>() {
                    @Override
                    public void call(final Subscriber<? super ContentValues> subscriber) {
                        Firebase ref = new Firebase("https://hacker-news.firebaseio.com/item/" + id);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();
                                subscriber.onNext(mapStory(newItem));
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                });
            }
        }).toList();

        Observable.zip(listObservable, listObservable, new Func2<List<ContentValues>, List<ContentValues>, Vector<ContentValues>>() {
            @Override
            public Vector<ContentValues> call(List<ContentValues> topStories, List<ContentValues> newStories) {
                final Vector<ContentValues> storiesList = new Vector<>();

                for (ContentValues story : topStories){
                    storiesList.add(story);
                }

                for (ContentValues story : newStories) {
                    storiesList.add(story);
                }

                return storiesList;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Observer<Vector<ContentValues>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Vector<ContentValues> stories) {
                preferences.saveRefreshTick(type);
                persister.persistStories(stories);
            }
        });

    }

    private ContentValues mapStory(Map<String, Object> map) {

        String by = (String) map.get("by");
        Long id = (Long) map.get("id");
        String type = (String) map.get("type");
        long time = (long) map.get("time");
        long score = (Long) map.get("score");
        String title = (String) map.get("title");
        String url = (String) map.get("url");

        ContentValues storyValues = new ContentValues();

        storyValues.put(HNewsContract.StoryEntry.ITEM_ID, id);
        storyValues.put(HNewsContract.StoryEntry.BY, by);
        storyValues.put(HNewsContract.StoryEntry.TYPE, type);
        storyValues.put(HNewsContract.StoryEntry.TIMESTAMP, time * 1000);
        storyValues.put(HNewsContract.StoryEntry.SCORE, score);
        storyValues.put(HNewsContract.StoryEntry.TITLE, title);
        storyValues.put(HNewsContract.StoryEntry.URL, url);

        return storyValues;
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

    private static class StoriesObserver implements Observable.OnSubscribe<Long> {

        private final Story.TYPE type;

        public StoriesObserver(Story.TYPE type) {
            this.type = type;
        }

        @Override
        public void call(final Subscriber<? super Long> subscriber) {
            Firebase topStories = new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
            topStories.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                    Long itemId = (Long) snapshot.getValue();
                    subscriber.onNext(itemId);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });
        }
    }

}
