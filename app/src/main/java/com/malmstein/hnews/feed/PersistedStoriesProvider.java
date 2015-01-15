package com.malmstein.hnews.feed;

import com.malmstein.hnews.base.ForwardingSubject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersistedStoriesProvider implements StoriesProvider {

    private final NewsRetriever<StoriesUpdateEvent> retriever;

    private final ForwardingSubject<StoriesUpdateEvent> feedUpdateEventSubject;
    private final CachedOnSubscribe<StoriesUpdateEvent> feedUpdateEventOnSubscribe;

    private Subscription retrieverSubscription;

    public PersistedStoriesProvider(NewsRetriever<StoriesUpdateEvent> retriever) {
        this.retriever = retriever;
        this.feedUpdateEventOnSubscribe = new NewsUpdateEventCachedOnSubscribe();
        this.feedUpdateEventSubject = new ForwardingSubject<>(feedUpdateEventOnSubscribe);
    }

    @Override
    public void refresh() {
        retrieverSubscription = startRemoteRefresh()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    @Override
    public void fetchNewStories() {
        retrieverSubscription = startRemoteNewStoriesRefresh()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    @Override
    public void fetchBestStories() {
        retrieverSubscription = startRemoteBestStoriesRefresh()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    @Override
    public void fetchTopStories() {
        retrieverSubscription = startRemoteRefresh()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    @Override
    public void fetchShowStories() {
        retrieverSubscription = startRemoteShowStoriesRefresh()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    @Override
    public void fetchAskStories() {
        retrieverSubscription = startRemoteAskStoriesRefresh()
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    private Observable<StoriesUpdateEvent> startRemoteRefresh() {
        return retriever.fetchTop()
                .mergeWith(retriever.fetchNew())
                .mergeWith(retriever.fetchBest())
                .mergeWith(retriever.fetchShow())
                .mergeWith(retriever.fetchAsk());
    }

    private Observable<StoriesUpdateEvent> startRemoteNewStoriesRefresh() {
        return retriever.fetchNew();
    }

    private Observable<StoriesUpdateEvent> startRemoteBestStoriesRefresh() {
        return retriever.fetchTop();
    }

    private Observable<StoriesUpdateEvent> startRemoteShowStoriesRefresh() {
        return retriever.fetchShow();
    }

    private Observable<StoriesUpdateEvent> startRemoteAskStoriesRefresh() {
        return retriever.fetchAsk();
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