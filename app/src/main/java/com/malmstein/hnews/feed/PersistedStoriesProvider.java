package com.malmstein.hnews.feed;

import com.malmstein.hnews.base.ForwardingSubject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersistedStoriesProvider implements StoriesProvider {

    private final Retriever<StoriesUpdateEvent> retriever;

    private final ForwardingSubject<StoriesUpdateEvent> feedUpdateEventSubject;
    private final CachedOnSubscribe<StoriesUpdateEvent> feedUpdateEventOnSubscribe;

    private Subscription retrieverSubscription;

    public PersistedStoriesProvider(Retriever<StoriesUpdateEvent> retriever) {
        this.retriever = retriever;
        this.feedUpdateEventOnSubscribe = new NewsUpdateEventCachedOnSubscribe();
        this.feedUpdateEventSubject = new ForwardingSubject<StoriesUpdateEvent>(feedUpdateEventOnSubscribe);
    }

    @Override
    public void refresh() {
        retrieverSubscription = startRemoteFetch()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    private Observable<StoriesUpdateEvent> startRemoteFetch() {
        return retriever.fetch();
    }

    private Action1<StoriesUpdateEvent> onRetrieverFinish() {
        return new Action1<StoriesUpdateEvent>() {
            @Override
            public void call(StoriesUpdateEvent storiesUpdateEvent) {
                if (!storiesUpdateEvent.isRefreshFinished()) {
                    return;
                }
                if (isAlreadySubscribed()) {
                    retrieverSubscription.unsubscribe();
                }
            }
        };
    }

    private Action1<Throwable> onRetrieverError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (isAlreadySubscribed()) {
                    retrieverSubscription.unsubscribe();
                }
            }
        };
    }

    private boolean isAlreadySubscribed() {
        return retrieverSubscription != null && !retrieverSubscription.isUnsubscribed();
    }

}