package com.malmstein.hnews.feed;

import com.malmstein.hnews.http.ConnectionProvider;
import com.malmstein.hnews.tasks.FetchTopStoriesTask;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.malmstein.hnews.feed.StoriesUpdateEvent.Type.REFRESH_FINISHED;
import static com.malmstein.hnews.feed.StoriesUpdateEvent.Type.REFRESH_STARTED;

public class HNRetriever implements Retriever<StoriesUpdateEvent> {

    private final DatabasePersister databasePersister;
    private final ConnectionProvider connectionProvider;

    public HNRetriever(DatabasePersister databasePersister, ConnectionProvider connectionProvider) {
        this.databasePersister = databasePersister;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Observable<StoriesUpdateEvent> fetchStories() {
        return Observable.create(new StoriesUpdateOnSubscribe(databasePersister))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<StoriesUpdateEvent> fetchComments() {
        return null;
    }

    private static class StoriesUpdateOnSubscribe implements Observable.OnSubscribe<StoriesUpdateEvent> {

        private final DatabasePersister databasePersister;
        private Subscriber<? super StoriesUpdateEvent> subscriber;

        private StoriesUpdateOnSubscribe(DatabasePersister databasePersister) {
            this.databasePersister = databasePersister;
        }

        @Override
        public void call(Subscriber<? super StoriesUpdateEvent> subscriber) {
            this.subscriber = subscriber;
            startFetchingTopsStories();
            subscriber.onCompleted();
        }

        private void startFetchingTopsStories() {
            subscriber.onNext(new StoriesUpdateEvent(REFRESH_STARTED));
            createFetchTopStoriesTask();
            subscriber.onNext(new StoriesUpdateEvent(REFRESH_FINISHED));
        }

        private FetchTopStoriesTask createFetchTopStoriesTask() {
            return new FetchTopStoriesTask(databasePersister);
        }
    }
}