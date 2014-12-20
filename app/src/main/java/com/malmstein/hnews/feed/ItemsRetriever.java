package com.malmstein.hnews.feed;

import com.malmstein.hnews.tasks.FetchTopStoriesTask;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.malmstein.hnews.feed.NewsUpdateEvent.Type.REFRESH_FINISHED;
import static com.malmstein.hnews.feed.NewsUpdateEvent.Type.REFRESH_STARTED;

public class ItemsRetriever implements Retriever<NewsUpdateEvent> {

    private final NewsPersister newsPersister;

    public ItemsRetriever(NewsPersister newsPersister) {
        this.newsPersister = newsPersister;
    }

    @Override
    public Observable<NewsUpdateEvent> fetch() {
        return Observable.create(new FeedUpdateOnSubscribe(newsPersister))
                .subscribeOn(Schedulers.io());
    }

    private static class FeedUpdateOnSubscribe implements Observable.OnSubscribe<NewsUpdateEvent> {

        private final NewsPersister newsPersister;
        private Subscriber<? super NewsUpdateEvent> subscriber;

        private FeedUpdateOnSubscribe(NewsPersister newsPersister) {
            this.newsPersister = newsPersister;
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