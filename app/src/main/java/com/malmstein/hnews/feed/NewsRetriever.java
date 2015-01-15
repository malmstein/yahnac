package com.malmstein.hnews.feed;

import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.tasks.FetchShowHNTask;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class NewsRetriever<S> implements StoriesRetriever<StoriesUpdateEvent> {

    private final DatabasePersister databasePersister;

    public NewsRetriever(DatabasePersister databasePersister) {
        this.databasePersister = databasePersister;
    }

    @Override
    public Observable<StoriesUpdateEvent> fetch(Long... params) {
        return Observable.create(
                new StoriesUpdateOnSubscribe(databasePersister, Story.TYPE.show))
                    .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<StoriesUpdateEvent> fetchTop() {
        return Observable.create(
                new StoriesUpdateOnSubscribe(databasePersister, Story.TYPE.top_story))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<StoriesUpdateEvent> fetchNew() {
        return Observable.create(
                new StoriesUpdateOnSubscribe(databasePersister, Story.TYPE.new_story))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<StoriesUpdateEvent> fetchBest() {
        return Observable.create(
                new StoriesUpdateOnSubscribe(databasePersister, Story.TYPE.best_story))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<StoriesUpdateEvent> fetchShow() {
        return Observable.create(
                new StoriesUpdateOnSubscribe(databasePersister, Story.TYPE.show))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<StoriesUpdateEvent> fetchAsk() {
        return Observable.create(
                new StoriesUpdateOnSubscribe(databasePersister, Story.TYPE.ask))
                .subscribeOn(Schedulers.io());
    }

    private static class StoriesUpdateOnSubscribe implements Observable.OnSubscribe<StoriesUpdateEvent> {

        private final DatabasePersister databasePersister;
        private final Story.TYPE type;
        private Subscriber<? super StoriesUpdateEvent> subscriber;

        private StoriesUpdateOnSubscribe(DatabasePersister databasePersister, Story.TYPE type) {
            this.databasePersister = databasePersister;
            this.type = type;
        }

        @Override
        public void call(Subscriber<? super StoriesUpdateEvent> subscriber) {
            this.subscriber = subscriber;
            startFetchingStories();
            subscriber.onCompleted();
        }

        private void startFetchingStories() {
            subscriber.onNext(new StoriesUpdateEvent(StoriesUpdateEvent.Type.REFRESH_STARTED));
            try {
                switch (type){
                    case top_story:
                        createFetchTopStoriesTask().execute();
                        break;
                    case new_story:
                        createFetchTopStoriesTask().execute();
                        break;
                    case best_story:
                        createFetchTopStoriesTask().execute();
                        break;
                    case show:
                        createFetchTopStoriesTask().execute();
                        break;
                    case ask:
                        createFetchTopStoriesTask().execute();
                        break;
                }
            } catch (IOException e) {
                subscriber.onError(e);
            }
            subscriber.onNext(new StoriesUpdateEvent(StoriesUpdateEvent.Type.REFRESH_FINISHED));
        }

        private FetchShowHNTask createFetchTopStoriesTask() {
            return new FetchShowHNTask(databasePersister);
        }

    }

}