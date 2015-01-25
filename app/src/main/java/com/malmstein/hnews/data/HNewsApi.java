package com.malmstein.hnews.data;

import android.content.ContentValues;

import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.tasks.FetchCommentsTask;
import com.malmstein.hnews.tasks.FetchStoriesTask;

import java.io.IOException;
import java.util.Vector;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class HNewsApi {

    Observable<Vector<ContentValues>> getStories(Story.TYPE storyType) {
        return Observable.create(
                new StoriesUpdateOnSubscribe(storyType))
                .subscribeOn(Schedulers.io());
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

            subscriber.onNext(stories);
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
}
