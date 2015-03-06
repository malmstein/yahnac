package com.malmstein.hnews.data;

import android.content.ContentValues;

import com.malmstein.hnews.connectivity.WizMerlin;
import com.malmstein.hnews.model.StoriesJsoup;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.updater.RefreshSharedPreferences;
import com.malmstein.hnews.updater.RefreshTimestamp;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class DataRepository {

    private static final long MILLIS_IN_AMINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long maxMillisWithoutUpgrade = 60 * MILLIS_IN_AMINUTE;

    private final HNewsApi api;
    private final DataPersister dataPersister;
    private final WizMerlin merlin;
    private final RefreshSharedPreferences refreshPreferences;

    public DataRepository(DataPersister dataPersister, WizMerlin merlin) {
        this.dataPersister = dataPersister;
        this.merlin = merlin;
        this.api = new HNewsApi();
        this.refreshPreferences = RefreshSharedPreferences.newInstance();
    }

    public boolean shouldUpdateContent(Story.TYPE type) {
        if (merlin.detectsWorkingNetworkConnection()) {
            RefreshTimestamp lastUpdate = refreshPreferences.getLastRefresh(type);
            RefreshTimestamp now = RefreshTimestamp.now();
            long elapsedTime = now.getMillis() - lastUpdate.getMillis();
            return elapsedTime > maxMillisWithoutUpgrade;
        } else {
            return false;
        }
    }

    public Observable<String> observeStories(Story.TYPE type, String nextUrl) {
        return getStories(type, nextUrl);
    }

    private Observable<String> getStories(final Story.TYPE type, final String nextUrl) {
        if (merlin.detectsWorkingNetworkConnection()) {
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
        }else{
            return Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext("");
                    subscriber.onCompleted();
                }
            });
        }
    }

    public Observable<Integer> observeComments(final Long storyId) {
        if (merlin.detectsWorkingNetworkConnection()) {
            return getComments(storyId);
        } else {
            return Observable.create(new Observable.OnSubscribe<Integer>() {
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    subscriber.onNext(0);
                    subscriber.onCompleted();
                }
            });
        }
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
