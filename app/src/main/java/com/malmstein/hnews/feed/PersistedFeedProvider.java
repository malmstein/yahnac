package com.malmstein.hnews.feed;

import com.malmstein.hnews.base.ForwardingSubject;
import com.malmstein.hnews.model.Item;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersistedFeedProvider implements FeedProvider {

    private final Retriever<FeedUpdateEvent> retriever;
    private final FeedPersister feedPersister;
    private final ForwardingSubject<FeedUpdateEvent> feedUpdateEventSubject;
    private final CachedOnSubscribe<FeedUpdateEvent> feedUpdateEventOnSubscribe;

    private Subscription retrieverSubscription;

    public PersistedFeedProvider(Retriever<FeedUpdateEvent> retriever, FeedPersister feedPersister) {
        this.retriever = retriever;
        this.feedPersister = feedPersister;
        this.feedUpdateEventOnSubscribe = new FeedUpdateEventCachedOnSubscribe();
        this.feedUpdateEventSubject = new ForwardingSubject<FeedUpdateEvent>(feedUpdateEventOnSubscribe);
    }

    @Override
    public void refresh() {
        retrieverSubscription = startRemoteFetch()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    private Observable<FeedUpdateEvent> startRemoteFetch() {
        return retriever.fetch();
    }

    private Action1<FeedUpdateEvent> onRetrieverFinish() {
        return new Action1<FeedUpdateEvent>() {
            @Override
            public void call(FeedUpdateEvent feedUpdateEvent) {
                if (!feedUpdateEvent.isRefreshFinished()) {
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

    @Override
    public Observable<Item> getNewsObservable() {
        return null;
    }
}