package com.malmstein.yahnac.data;

import android.content.ContentValues;

import com.malmstein.yahnac.data.updater.LoginSharedPreferences;
import com.malmstein.yahnac.data.updater.RefreshSharedPreferences;
import com.malmstein.yahnac.data.updater.RefreshTimestamp;
import com.malmstein.yahnac.model.Login;
import com.malmstein.yahnac.model.OperationResponse;
import com.malmstein.yahnac.model.Story;

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
    private Func1<Throwable, OperationResponse> loginExpired = new Func1<Throwable, OperationResponse>() {
        @Override
        public OperationResponse call(Throwable throwable) {
            if (throwable instanceof LoggedOutException) {
                return OperationResponse.LOGIN_EXPIRED;
            }
            return OperationResponse.FAILURE;
        }
    };

    public Provider(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
        this.api = new HNewsApi();
        this.refreshPreferences = RefreshSharedPreferences.newInstance();
        this.loginSharedPreferences = LoginSharedPreferences.newInstance();
    }

    public boolean shouldUpdateContent(Story.FILTER FILTER) {
        if (FILTER == Story.FILTER.best_story) {
            return false;
        }
        RefreshTimestamp lastUpdate = refreshPreferences.getLastRefresh(FILTER);
        RefreshTimestamp now = RefreshTimestamp.now();
        long elapsedTime = now.getMillis() - lastUpdate.getMillis();
        return elapsedTime > maxMillisWithoutUpgrade;
    }

    public Observable<Integer> getStories(final Story.FILTER FILTER) {
        return api.getStories(FILTER)
                .flatMap(new Func1<List<ContentValues>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final List<ContentValues> stories) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                refreshPreferences.saveRefreshTick(FILTER);
                                dataPersister.persistStories(stories);
                                subscriber.onNext(stories.size());
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

    public Observable<OperationResponse> observeComments(final Long storyId) {
        return api.getCommentsFromStory(storyId)
                .flatMap(new Func1<Vector<ContentValues>, Observable<OperationResponse>>() {
                    @Override
                    public Observable<OperationResponse> call(final Vector<ContentValues> commentsJsoup) {
                        return Observable.create(new Observable.OnSubscribe<OperationResponse>() {
                            @Override
                            public void call(Subscriber<? super OperationResponse> subscriber) {
                                dataPersister.persistComments(commentsJsoup, storyId);
                                subscriber.onNext(OperationResponse.SUCCESS);
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

    public Observable<OperationResponse> observeVote(final Story story) {
        return api.vote(story)
                .flatMap(new Func1<OperationResponse, Observable<OperationResponse>>() {
                    @Override
                    public Observable<OperationResponse> call(final OperationResponse response) {
                        return Observable.create(new Observable.OnSubscribe<OperationResponse>() {
                            @Override
                            public void call(Subscriber<? super OperationResponse> subscriber) {
                                if (response.equals(OperationResponse.SUCCESS)) {
                                    dataPersister.addVote(story);
                                }
                                subscriber.onNext(response);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).onErrorReturn(loginExpired);
    }

    public Observable<OperationResponse> observeCommentOnStory(final long storyId, final String message) {
        return api.commentOnStory(storyId, message)
                .flatMap(new Func1<OperationResponse, Observable<OperationResponse>>() {
                    @Override
                    public Observable<OperationResponse> call(final OperationResponse response) {
                        return Observable.create(new Observable.OnSubscribe<OperationResponse>() {
                            @Override
                            public void call(Subscriber<? super OperationResponse> subscriber) {
                                subscriber.onNext(response);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).onErrorReturn(loginExpired);
    }

    public Observable<OperationResponse> observeReplyToComment(final long storyId, final long commentId, final String message) {
        return api.replyToComment(storyId, commentId, message)
                .flatMap(new Func1<OperationResponse, Observable<OperationResponse>>() {
                    @Override
                    public Observable<OperationResponse> call(final OperationResponse response) {
                        return Observable.create(new Observable.OnSubscribe<OperationResponse>() {
                            @Override
                            public void call(Subscriber<? super OperationResponse> subscriber) {
                                subscriber.onNext(response);
                                subscriber.onCompleted();
                            }
                        });
                    }
                }).onErrorReturn(loginExpired);
    }

}
