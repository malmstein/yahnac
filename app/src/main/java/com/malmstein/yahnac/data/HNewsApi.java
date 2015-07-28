package com.malmstein.yahnac.data;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Pair;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.malmstein.yahnac.comments.CommentsParser;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Login;
import com.malmstein.yahnac.model.Story;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

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
                                    ContentValues story = mapStory(newItem, type, storyRoot.first);
                                    if (story != null) {
                                        subscriber.onNext(story);
                                    } else {
                                        subscriber.onNext(new ContentValues());
                                        Inject.crashAnalytics().logSomethingWentWrong("HNewsApi: onDataChange is empty in " + storyRoot.second);
                                    }
                                }
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                Log.d(firebaseError.getCode());
                                Inject.crashAnalytics().logSomethingWentWrong("HNewsApi: onCancelled " + firebaseError.getMessage());
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
            }
        })
                .toList();
    }

    private ContentValues mapStory(Map<String, Object> map, Story.TYPE rootType, Integer rank) {

        ContentValues storyValues = new ContentValues();

        try {
            String by = (String) map.get("by");
            Long id = (Long) map.get("id");
            String type;
            if (rootType == Story.TYPE.best_story) {
                type = Story.TYPE.top_story.name();
            } else {
                type = rootType.name();
            }
            Long time = (Long) map.get("time");
            Long score = (Long) map.get("score");
            String title = (String) map.get("title");
            String url = (String) map.get("url");
            Long descendants = Long.valueOf(0);
            if (map.get("descendants") != null) {
                descendants = (Long) map.get("descendants");
            }

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
        } catch (Exception ex) {
            Log.d(ex.getMessage());
        }

        return storyValues;
    }

    private Firebase getStoryFirebase(Story.TYPE type) {
        switch (type) {
            case top_story:
                return new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
            case new_story:
                return new Firebase("https://hacker-news.firebaseio.com/v0/newstories");
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

    Observable<Vector<ContentValues>> getCommentsFromStory(Long storyId) {
        return Observable.create(
                new CommentsUpdateOnSubscribe(storyId))
                .subscribeOn(Schedulers.io());
    }

    Observable<Login> login(String username, String password) {
        return Observable.create(
                new LoginOnSubscribe(username, password))
                .subscribeOn(Schedulers.io());
    }

    Observable<String> vote(Story storyId, String username, String auth) {
        return Observable.create(
                new VoteOnSubscribe(storyId, username, auth))
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String dataSnapshot) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {

                                subscriber.onNext("url");
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
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
                ConnectionProvider connectionProvider = Inject.connectionProvider();
                Document commentsDocument = connectionProvider
                        .commentsConnection(storyId)
                        .get();

                commentsList = new CommentsParser(storyId, commentsDocument).parse();

            } catch (IOException e) {
                subscriber.onError(e);
            }
            subscriber.onNext(commentsList);
        }
    }

    private static class LoginOnSubscribe implements Observable.OnSubscribe<Login> {

        private final String username;
        private final String password;
        private Subscriber<? super Login> subscriber;

        private LoginOnSubscribe(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void call(Subscriber<? super Login> subscriber) {
            this.subscriber = subscriber;
            attemptLogin();
            subscriber.onCompleted();
        }

        private void attemptLogin() {
            try {
                ConnectionProvider connectionProvider = Inject.connectionProvider();
                Connection.Response response = connectionProvider
                        .loginConnection(username, password)
                        .execute();

                String cookie = response.cookie("user");
                String cfduid = response.cookie("_cfduid");

                if (!TextUtils.isEmpty(cookie)) {
                    subscriber.onNext(new Login(username, cookie, Login.Status.SUCCESSFUL));
                } else {
                    subscriber.onNext(new Login(username, null, Login.Status.WRONG_CREDENTIALS));
                }

            } catch (IOException e) {
                subscriber.onError(e);
            }
        }
    }

    private static class ParseVoteUrlOnSubscribe implements Observable.OnSubscribe<String> {

        private final Long storyId;
        private Subscriber<? super String> subscriber;

        private ParseVoteUrlOnSubscribe(Long storyId) {
            this.storyId = storyId;
        }

        @Override
        public void call(Subscriber<? super String> subscriber) {
            this.subscriber = subscriber;
            startFetchingVoteUrl();
            subscriber.onCompleted();
        }

        private void startFetchingVoteUrl() {
            Vector<ContentValues> commentsList = new Vector<>();
            try {
                ConnectionProvider connectionProvider = Inject.connectionProvider();
                Document commentsDocument = connectionProvider
                        .commentsConnection(storyId)
                        .get();

                commentsList = new CommentsParser(storyId, commentsDocument).parse();

            } catch (IOException e) {
                subscriber.onError(e);
            }
            subscriber.onNext("voteUrl");
        }
    }

    private static class VoteOnSubscribe implements Observable.OnSubscribe<String> {

        private static final String BAD_UPVOTE_RESPONSE = "Can't make that vote.";

        private final Story story;
        private final String username;
        private final String auth;

        private Subscriber<? super String> subscriber;

        private VoteOnSubscribe(Story story, String username, String auth) {
            this.story = story;
            this.username = username;
            this.auth = auth;
        }

        @Override
        public void call(Subscriber<? super String> subscriber) {
            this.subscriber = subscriber;
            attemptVote();
            subscriber.onCompleted();
        }

        private void attemptVote() {
            try {
                ConnectionProvider connectionProvider = Inject.connectionProvider();
                Connection.Response response = connectionProvider
                        .voteConnection(story.getVoteUrl(username, auth))
                        .execute();

                if (response.statusCode() == 200) {
                    if (response.body() == null) {
                        subscriber.onError(new Throwable(""));
                    }

                    Document doc = response.parse();
                    String text = doc.text();

                    if (text.equals(BAD_UPVOTE_RESPONSE)) {
                        subscriber.onNext(BAD_UPVOTE_RESPONSE);
                    } else {
                        subscriber.onNext(text);
                    }
                }

            } catch (IOException e) {
                subscriber.onError(e);
            }
        }
    }
}
