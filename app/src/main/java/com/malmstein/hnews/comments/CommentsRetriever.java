package com.malmstein.hnews.comments;

import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.Retriever;
import com.malmstein.hnews.http.ConnectionProvider;
import com.malmstein.hnews.tasks.FetchCommentsTask;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.malmstein.hnews.comments.CommentsUpdateEvent.Type.REFRESH_FINISHED;
import static com.malmstein.hnews.comments.CommentsUpdateEvent.Type.REFRESH_STARTED;

public class CommentsRetriever implements Retriever<CommentsUpdateEvent> {

    private final DatabasePersister databasePersister;
    private final ConnectionProvider connectionProvider;

    public CommentsRetriever(DatabasePersister databasePersister, ConnectionProvider connectionProvider) {
        this.databasePersister = databasePersister;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Observable<CommentsUpdateEvent> fetch() {
        return Observable.create(new CommentsUpdateOnSubscribe(databasePersister, connectionProvider))
                .subscribeOn(Schedulers.io());
    }

    private static class CommentsUpdateOnSubscribe implements Observable.OnSubscribe<CommentsUpdateEvent> {

        private final DatabasePersister databasePersister;
        private final ConnectionProvider connectionProvider;
        private Subscriber<? super CommentsUpdateEvent> subscriber;

        private CommentsUpdateOnSubscribe(DatabasePersister databasePersister, ConnectionProvider connectionProvider) {
            this.databasePersister = databasePersister;
            this.connectionProvider = connectionProvider;
        }

        @Override
        public void call(Subscriber<? super CommentsUpdateEvent> subscriber) {
            this.subscriber = subscriber;
            startFetchingComments();
            subscriber.onCompleted();
        }

        private void startFetchingComments() {
            subscriber.onNext(new CommentsUpdateEvent(REFRESH_STARTED));
            createFetchCommentsTask();
            subscriber.onNext(new CommentsUpdateEvent(REFRESH_FINISHED));
        }


        private FetchCommentsTask createFetchCommentsTask() {
            return new FetchCommentsTask(databasePersister, connectionProvider);
        }
    }
}