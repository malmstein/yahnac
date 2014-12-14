package com.malmstein.hnews.base;

import com.malmstein.hnews.feed.CachedOnSubscribe;

public class ForwardingSubject<T> extends rx.subjects.Subject<T, T> {

    private final CachedOnSubscribe<T> subscribeFunc;

    public ForwardingSubject(CachedOnSubscribe<T> subscribeFunc) {
        super(subscribeFunc);
        this.subscribeFunc = subscribeFunc;
    }

    @Override
    public void onCompleted() {
        subscribeFunc.onCompleted();
    }

    @Override
    public void onError(Throwable throwable) {
        subscribeFunc.onError(throwable);
    }

    @Override
    public void onNext(T t) {
        subscribeFunc.onNext(t);
    }

    @Override
    public boolean hasObservers() {
        return true;
    }

}
