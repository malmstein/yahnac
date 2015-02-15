package com.malmstein.hnews.data;

import android.content.ContentValues;

import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.updater.RefreshSharedPreferences;
import com.malmstein.hnews.updater.RefreshTimestamp;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class DataRepository {

    private static final long MILLIS_IN_AMINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long maxMillisWithoutUpgrade = 60 * MILLIS_IN_AMINUTE;

    private final BehaviorSubject<Integer> storySubject;
    private final BehaviorSubject<Integer> commentsSubject;
    private final HNewsApi api;
    private final DataPersister dataPersister;
    private final RefreshSharedPreferences refreshPreferences;

    public DataRepository(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
        this.storySubject = BehaviorSubject.create();
        this.commentsSubject = BehaviorSubject.create();
        this.api = new HNewsApi();
        this.refreshPreferences = RefreshSharedPreferences.newInstance();
    }

    public boolean shouldUpdateContent(Story.TYPE type){
        RefreshTimestamp lastUpdate = refreshPreferences.getLastRefresh(type);
        RefreshTimestamp now = RefreshTimestamp.now();
        long elapsedTime = now.getMillis() - lastUpdate.getMillis();
        return elapsedTime > maxMillisWithoutUpgrade;
    }


    public Observable<Integer> getStoriesFrom(Story.TYPE type) {
        refreshStoryType(type);
        return storySubject;
    }

    private void refreshStoryType(final Story.TYPE type) {
        api.getStories(type)
                .flatMap(new Func1<Vector<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Vector<ContentValues> contentValueses) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                refreshPreferences.saveRefreshTick(type);
                                dataPersister.persistStories(contentValueses);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io()).subscribe(storySubject);
    }

    public Observable<Integer> getCommentsFromStory(Long storyId) {
        refreshComments(storyId);
        return storySubject;
    }

    private void refreshComments(final Long storyId) {
        api.getCommentsFromStory(storyId)
                .flatMap(new Func1<Vector<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Vector<ContentValues> contentValueses) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                dataPersister.persistComments(contentValueses, storyId);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io()).subscribe(commentsSubject);
    }

}
