package com.malmstein.hnews.comments;

import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.Retriever;
import com.malmstein.hnews.tasks.FetchCommentsTask;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.malmstein.hnews.comments.CommentsUpdateEvent.Type.REFRESH_FINISHED;
import static com.malmstein.hnews.comments.CommentsUpdateEvent.Type.REFRESH_STARTED;

public class CommentsRetriever implements Retriever<CommentsUpdateEvent>{

    private final DatabasePersister databasePersister;

    public CommentsRetriever(DatabasePersister databasePersister) {
        this.databasePersister = databasePersister;
    }

    @Override
    public Observable<CommentsUpdateEvent> fetch(Long... params) {
        return Observable.create(new CommentsUpdateOnSubscribe(databasePersister, params[0]))
                .subscribeOn(Schedulers.io());
    }

    private static class CommentsUpdateOnSubscribe implements Observable.OnSubscribe<CommentsUpdateEvent> {

        private final DatabasePersister databasePersister;
        private final Long storyId;
        private Subscriber<? super CommentsUpdateEvent> subscriber;

        private CommentsUpdateOnSubscribe(DatabasePersister databasePersister, Long storyId) {
            this.databasePersister = databasePersister;
            this.storyId = storyId;
        }

        @Override
        public void call(Subscriber<? super CommentsUpdateEvent> subscriber) {
            this.subscriber = subscriber;
            startFetchingComments();
            subscriber.onCompleted();
        }

        private void startFetchingComments() {
            subscriber.onNext(new CommentsUpdateEvent(REFRESH_STARTED));
            try {
                createFetchCommentsTask().execute();
            } catch (IOException e) {
                subscriber.onError(e);
            }
            subscriber.onNext(new CommentsUpdateEvent(REFRESH_FINISHED));
        }

        private FetchCommentsTask createFetchCommentsTask() {
            return new FetchCommentsTask(databasePersister, storyId);
        }
    }
}