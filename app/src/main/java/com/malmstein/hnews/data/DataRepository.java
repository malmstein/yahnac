package com.malmstein.hnews.data;

import android.content.ContentValues;

import com.malmstein.hnews.model.CommentsJsoup;
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

    public Observable<Integer> getStoriesFrom(Story.TYPE type) {
        return refreshStoryType(type);
    }

    private Observable<Integer> refreshStoryType(final Story.TYPE type) {
        return api.getStories(type)
                .flatMap(new Func1<Vector<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Vector<ContentValues> contentValueses) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                refreshPreferences.saveRefreshTick(type);
                                subscriber.onNext(dataPersister.persistStories(contentValueses));
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

    public Observable<String> getCommentsFromStory(Long storyId) {
        return refreshComments(storyId);
    }

    private Observable<String> refreshComments(final Long storyId) {
        return api.getCommentsFromStory(storyId)
                .flatMap(new Func1<CommentsJsoup, Observable<String>>() {
                    @Override
                    public Observable<String> call(final CommentsJsoup commentsJsoup) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                dataPersister.persistComments(commentsJsoup.getCommentsList(), storyId);
                                subscriber.onNext(commentsJsoup.getTitle());
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

}
