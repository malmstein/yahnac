package com.malmstein.hnews.data;

import android.content.ContentValues;

import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.tasks.FetchCommentsTask;
import com.malmstein.hnews.tasks.FetchStoriesTask;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HNewsApi {

    Observable<Vector<ContentValues>> getStories(Story.TYPE storyType) {
        return retryObservable(Observable.create(
                new StoriesUpdateOnSubscribe(storyType))
                .subscribeOn(Schedulers.io()), 3, 3000);
    }

    private static class StoriesUpdateOnSubscribe implements Observable.OnSubscribe<Vector<ContentValues>> {

        private final Story.TYPE type;
        private Subscriber<? super Vector<ContentValues>> subscriber;

        private StoriesUpdateOnSubscribe(Story.TYPE type) {
            this.type = type;
        }

        @Override
        public void call(Subscriber<? super Vector<ContentValues>> subscriber) {
            this.subscriber = subscriber;
            startFetchingStories();
            subscriber.onCompleted();
        }

        private void startFetchingStories() {
            Vector<ContentValues> stories = new Vector<>();
            try {
                stories = new FetchStoriesTask(type).execute();
            } catch (IOException e) {
                subscriber.onError(e);
            }

            if (stories.size() == 0) {
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
            Vector<ContentValues> comments = new Vector<>();
            try {
                comments = new FetchCommentsTask(storyId).execute();
            } catch (IOException e) {
                subscriber.onError(e);
            }

            subscriber.onNext(comments);
        }

    }

    private static <T> Observable<T> retryObservable(final Observable<T> requestObservable, final int nbRetry, final long seconds) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(final Subscriber<? super T> subscriber) {
                requestObservable.subscribe(new Action1<T>() {
                                                @Override
                                                public void call(T arg0) {
                                                    subscriber.onNext(arg0);
                                                    subscriber.onCompleted();
                                                }
                                            },

                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable error) {
                                if (nbRetry > 0) {
                                    Observable.just(requestObservable)
                                            .delay(seconds, TimeUnit.SECONDS)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Action1<Observable<T>>() {
                                                @Override
                                                public void call(Observable<T> observable) {
                                                    retryObservable(observable,
                                                            nbRetry - 1, seconds)
                                                            .subscribe(subscriber);
                                                }
                                            });
                                } else {
                                    subscriber.onError(error);
                                }

                            }
                        });

            }

        });

    }
}
