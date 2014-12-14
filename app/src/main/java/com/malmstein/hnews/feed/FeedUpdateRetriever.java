package com.malmstein.hnews.feed;

import com.malmstein.hnews.http.ConnectionProvider;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.malmstein.hnews.feed.FeedUpdateEvent.Type.*;

public class FeedUpdateRetriever implements Retriever<FeedUpdateEvent> {

    private final ConnectionProvider connectionProvider;
    private final FeedPersister feedPersister;

    public FeedUpdateRetriever(FeedPersister feedPersister, ConnectionProvider connectionProvider) {
        this.feedPersister = feedPersister;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Observable<FeedUpdateEvent> fetch() {
        return Observable.create(new FeedUpdateOnSubscribe(feedPersister, connectionProvider))
                .subscribeOn(Schedulers.io());
    }

    private static class FeedUpdateOnSubscribe implements Observable.OnSubscribe<FeedUpdateEvent> {

        private final FeedPersister feedPersister;
        private final ConnectionProvider connectionProvider;
        private Subscriber<? super FeedUpdateEvent> subscriber;

        private FeedUpdateOnSubscribe(FeedPersister feedPersister, ConnectionProvider connectionProvider) {
            this.feedPersister = feedPersister;
            this.connectionProvider = connectionProvider;
        }

        @Override
        public void call(Subscriber<? super FeedUpdateEvent> subscriber) {
            this.subscriber = subscriber;
            startFetchingTask();
            subscriber.onCompleted();
        }

        private void startFetchingTask() {
            subscriber.onNext(new FeedUpdateEvent(REFRESH_STARTED));
            createFetchFeedTask(cookie).execute();
            subscriber.onNext(new FeedUpdateEvent(REFRESH_FINISHED));
        }

        private FetchFeedTask createFetchFeedTask(String cookie) {
            NewerFeedUrl newerUrl = feedRepository.getNewerFeedUrl();
            FetchFeedTask task;
            if (newerUrl.isValid()) {
                task = new FetchFeedTask(newerUrl.toUri(), cookie, logger, feedPersister, connectionProvider);
            } else {
                task = new FetchFeedTask(tokenType.getHost(), cookie, logger, feedPersister, connectionProvider);
            }
            return task;
        }
    }
}