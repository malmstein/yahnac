package com.malmstein.hnews.feed;

import com.malmstein.hnews.http.ConnectionProvider;
import com.malmstein.hnews.tasks.FetchTopStoriesTask;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.malmstein.hnews.feed.NewsUpdateEvent.Type.REFRESH_FINISHED;
import static com.malmstein.hnews.feed.NewsUpdateEvent.Type.REFRESH_STARTED;

public class NewsUpdateRetriever implements Retriever<NewsUpdateEvent> {

    private final ConnectionProvider connectionProvider;
    private final NewsPersister newsPersister;

    public NewsUpdateRetriever(NewsPersister newsPersister, ConnectionProvider connectionProvider) {
        this.newsPersister = newsPersister;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Observable<NewsUpdateEvent> fetch() {
        return Observable.create(new FeedUpdateOnSubscribe(newsPersister, connectionProvider))
                .subscribeOn(Schedulers.io());
    }

    private static class FeedUpdateOnSubscribe implements Observable.OnSubscribe<NewsUpdateEvent> {

        private final NewsPersister newsPersister;
        private final ConnectionProvider connectionProvider;
        private Subscriber<? super NewsUpdateEvent> subscriber;

        private FeedUpdateOnSubscribe(NewsPersister newsPersister, ConnectionProvider connectionProvider) {
            this.newsPersister = newsPersister;
            this.connectionProvider = connectionProvider;
        }

        @Override
        public void call(Subscriber<? super NewsUpdateEvent> subscriber) {
            this.subscriber = subscriber;
            startFetchingTopsStories();
            subscriber.onCompleted();
        }

        private void startFetchingTopsStories() {
            subscriber.onNext(new NewsUpdateEvent(REFRESH_STARTED));
            createFetchTopStoriesTask();
            subscriber.onNext(new NewsUpdateEvent(REFRESH_FINISHED));
        }

        private FetchTopStoriesTask createFetchTopStoriesTask() {
            return new FetchTopStoriesTask(newsPersister);
        }
    }
}