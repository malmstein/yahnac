package com.malmstein.hnews.feed;

import com.malmstein.hnews.base.ForwardingSubject;
import com.malmstein.hnews.model.Item;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersistedNewsProvider implements NewsProvider {

    private final Retriever<NewsUpdateEvent> retriever;

    private final ForwardingSubject<NewsUpdateEvent> feedUpdateEventSubject;
    private final CachedOnSubscribe<NewsUpdateEvent> feedUpdateEventOnSubscribe;

    private Subscription retrieverSubscription;

    public PersistedNewsProvider(Retriever<NewsUpdateEvent> retriever) {
        this.retriever = retriever;
        this.feedUpdateEventOnSubscribe = new NewsUpdateEventCachedOnSubscribe();
        this.feedUpdateEventSubject = new ForwardingSubject<NewsUpdateEvent>(feedUpdateEventOnSubscribe);
    }

    @Override
    public void refresh() {
        retrieverSubscription = startRemoteFetch()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    private Observable<NewsUpdateEvent> startRemoteFetch() {
        return retriever.fetch();
    }

    private Action1<NewsUpdateEvent> onRetrieverFinish() {
        return new Action1<NewsUpdateEvent>() {
            @Override
            public void call(NewsUpdateEvent newsUpdateEvent) {
                if (!newsUpdateEvent.isRefreshFinished()) {
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