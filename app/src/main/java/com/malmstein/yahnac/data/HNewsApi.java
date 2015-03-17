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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class HNewsApi {

    private Observable<String> getStories(final Story.TYPE type) {
        Observable<List<Object>> listObservable = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(final Subscriber<? super Long> subscriber) {
                Firebase ref = new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Long storyId = (Long) dataSnapshot.getValue();
                        subscriber.onNext(storyId);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
            }
        }).flatMap(new Func1<Long, Observable<Vector<ContentValues>>>() {
            @Override
            public Observable<Vector<ContentValues>> call(Long storyId) {
                Firebase ref = new Firebase("https://hacker-news.firebaseio.com/item/" + storyId);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         return
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            })
        }).toList();

        Observable.zip(listObservable, another, new Func2<List<Object>, Object, Object>() {
            @Override
            public Object call(List<Object> objects, Object o) {
                return null;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new PersisterObserver());

        return api.getStories(type, nextUrl)
                .flatMap(new Func1<StoriesJsoup, Observable<String>>() {
                    @Override
                    public Observable<String> call(final StoriesJsoup stories) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                refreshPreferences.saveRefreshTick(type);
                                dataPersister.persistStories(stories.getStories());
                                subscriber.onNext(stories.getNextUrl());
                                subscriber.onCompleted();
                            }
                        });
                    }
                });

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

    public void getStories(Story.TYPE type) {

        Vector<ContentValues> storiesList = new Vector<>();

        Observable.create(new StoriesObserver(type))
                .concatWith(new StoryObserver())
                .flatMap(itemFunc)
                .observeOn(AndroidSchedulers.mainThread());

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

    private Func1 itemFunc = new Func1<Long, Observable<Vector<ContentValues>>>() {
        @Override
        public Observable<Vector<ContentValues>> call(Long itemId) {
            Firebase item = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + itemId);
            item.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    };

    private static class StoryObserver implements Observable.OnSubscribe<Map<String, Object>> {

        public StoryObserver(Map<String, Object> newItem) {

        }

        @Override
        public void call(Subscriber<? super Map<String, Object>> subscriber) {

        }
    }

}
