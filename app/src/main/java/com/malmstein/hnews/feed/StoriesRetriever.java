package com.malmstein.hnews.feed;

import com.malmstein.hnews.tasks.FetchShowHNTask;
import com.malmstein.hnews.tasks.FetchTopStoriesTask;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class StoriesRetriever implements Retriever<StoriesUpdateEvent> {

    private final DatabasePersister databasePersister;

    public StoriesRetriever(DatabasePersister databasePersister) {
        this.databasePersister = databasePersister;
    }

    @Override
    public Observable<StoriesUpdateEvent> fetch(Long... params) {
        return Observable.create(new StoriesUpdateOnSubscribe(databasePersister))
                .subscribeOn(Schedulers.io());
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
            moveFromTmpToStories();
            subscriber.onCompleted();
        }

        private void startFetchingTopsStories() {
            subscriber.onNext(new StoriesUpdateEvent(StoriesUpdateEvent.Type.REFRESH_STARTED));
//            createFetchTopStoriesTask();
            try {
                new FetchShowHNTask().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            subscriber.onNext(new StoriesUpdateEvent(StoriesUpdateEvent.Type.REFRESH_FINISHED));
        }

        private void moveFromTmpToStories(){
            databasePersister.moveFromTmpToStories();
        }

        private FetchTopStoriesTask createFetchTopStoriesTask() {
            return new FetchTopStoriesTask(databasePersister);
        }

    }

}