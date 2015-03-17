package com.malmstein.yahnac.data;

import android.content.ContentValues;

import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.StoriesJsoup;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.tasks.FetchCommentsTask;
import com.malmstein.yahnac.updater.RefreshSharedPreferences;
import com.malmstein.yahnac.updater.RefreshTimestamp;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class DataRepository {

    private static final long MILLIS_IN_AMINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long maxMillisWithoutUpgrade = 60 * MILLIS_IN_AMINUTE;

    private final HNewsApi api;
    private final DataPersister dataPersister;
    private final RefreshSharedPreferences refreshPreferences;

    public DataRepository(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
        this.api = new HNewsApi();
        this.refreshPreferences = RefreshSharedPreferences.newInstance();
    }

    public boolean shouldUpdateContent(Story.TYPE type) {
        RefreshTimestamp lastUpdate = refreshPreferences.getLastRefresh(type);
        RefreshTimestamp now = RefreshTimestamp.now();
        long elapsedTime = now.getMillis() - lastUpdate.getMillis();
        return elapsedTime > maxMillisWithoutUpgrade;

    }

    public Observable<String> observeStories(Story.TYPE type) {
        return api.getStories(type);
    }

    private Observable<String> getStories(final Story.TYPE type, final String nextUrl) {
        Observable<List<Object>> listObservable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext(1);
            }
        }).flatMap(new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object o) {
                return api.getFor(o);
            }
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

    private static class PersisterObserver implements Observable.OnSubscribe<Vector<ContentValues>> {

        private Subscriber<? super Vector<ContentValues>> subscriber;

        private PersisterObserver(Long storyId) {
            this.storyId = storyId;
        }

        @Override
        public void call(Subscriber<? super Vector<ContentValues>> subscriber) {
            this.subscriber = subscriber;
            dataPersister.persistStories(stories.getStories());
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

    public Observable<Integer> observeComments(final Long storyId) {
        return getComments(storyId);
    }

    private Observable<Integer> getComments(final Long storyId) {
        return api.getCommentsFromStory(storyId)
                .flatMap(new Func1<Vector<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Vector<ContentValues> commentsJsoup) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                subscriber.onNext(dataPersister.persistComments(commentsJsoup, storyId));
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

}
