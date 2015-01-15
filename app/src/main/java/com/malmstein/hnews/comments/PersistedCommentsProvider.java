package com.malmstein.hnews.comments;

import com.malmstein.hnews.base.ForwardingSubject;
import com.malmstein.hnews.feed.CachedOnSubscribe;
import com.malmstein.hnews.feed.Retriever;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersistedCommentsProvider implements CommentsProvider{

    private final Retriever<CommentsUpdateEvent> retriever;

    private final ForwardingSubject<CommentsUpdateEvent> feedUpdateEventSubject;
    private final CachedOnSubscribe<CommentsUpdateEvent> feedUpdateEventOnSubscribe;

    private Subscription retrieverSubscription;

    public PersistedCommentsProvider(Retriever<CommentsUpdateEvent> retriever) {
        this.retriever = retriever;
        this.feedUpdateEventOnSubscribe = new CommentsUpdateEventCachedOnSubscribe();
        this.feedUpdateEventSubject = new ForwardingSubject<>(feedUpdateEventOnSubscribe);
    }

    @Override
    public void fetch(Long storyId) {
        retrieverSubscription = startRemoteFetch(storyId)
                .doOnNext(onRetrieverFinish())
                .doOnError(onRetrieverError())
                .subscribeOn(Schedulers.io())
                .subscribe(feedUpdateEventSubject);
    }

    private Observable<CommentsUpdateEvent> startRemoteFetch(Long storyId) {
        return retriever.fetch(storyId);
    }

    private Action1<CommentsUpdateEvent> onRetrieverFinish() {
        return new Action1<CommentsUpdateEvent>() {
            @Override
            public void call(CommentsUpdateEvent storiesUpdateEvent) {
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