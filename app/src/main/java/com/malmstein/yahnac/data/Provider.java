package com.malmstein.yahnac.data;

import android.content.ContentValues;

import com.malmstein.yahnac.model.Login;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.updater.LoginSharedPreferences;
import com.malmstein.yahnac.updater.RefreshSharedPreferences;
import com.malmstein.yahnac.updater.RefreshTimestamp;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Provider {

    private static final long MILLIS_IN_AMINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long maxMillisWithoutUpgrade = 60 * MILLIS_IN_AMINUTE;

    private final HNewsApi api;
    private final DataPersister dataPersister;
    private final RefreshSharedPreferences refreshPreferences;
    private final LoginSharedPreferences loginSharedPreferences;

    public Provider(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
        this.api = new HNewsApi();
        this.refreshPreferences = RefreshSharedPreferences.newInstance();
        this.loginSharedPreferences = LoginSharedPreferences.newInstance();
    }

    public boolean shouldUpdateContent(Story.TYPE type) {
        if (type == Story.TYPE.best_story) {
            return false;
        }
        RefreshTimestamp lastUpdate = refreshPreferences.getLastRefresh(type);
        RefreshTimestamp now = RefreshTimestamp.now();
        long elapsedTime = now.getMillis() - lastUpdate.getMillis();
        return elapsedTime > maxMillisWithoutUpgrade;
    }


    public Observable<Integer> getStories(final Story.TYPE type) {
        return api.getStories(type)
                .flatMap(new Func1<List<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final List<ContentValues> stories) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                refreshPreferences.saveRefreshTick(type);
                                dataPersister.persistStories(stories);
                                subscriber.onNext(stories.size());
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

    public Observable<Integer> observeComments(final Long storyId) {
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

    public Observable<Login.Status> observeLogin(final String username, final String password) {
        return api.login(username, password)
                .flatMap(new Func1<Login, Observable<Login.Status>>() {
                    @Override
                    public Observable<Login.Status> call(final Login login) {
                        return Observable.create(new Observable.OnSubscribe<Login.Status>() {
                            @Override
                            public void call(Subscriber<? super Login.Status> subscriber) {
                                loginSharedPreferences.saveLogin(login);
                                subscriber.onNext(login.getStatus());
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

}
