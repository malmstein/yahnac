package com.malmstein.hnews.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

public class CachedOnSubscribe<T> implements Observable.OnSubscribe<T>, Observer<T> {

    private final List<Subscriber<? super T>> subscribers = Collections.synchronizedList(new ArrayList<Subscriber<? super T>>());
    private T cachedValue;

    public CachedOnSubscribe() {
    }

    public CachedOnSubscribe(T defaultValue) {
        cachedValue = defaultValue;
    }

    public boolean hasCachedValue() {
        return cachedValue != null;
    }

    public T getCachedValue() {
        return cachedValue;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        subscriber.add(new CachedSubscription(subscriber));
        subscribers.add(subscriber);
        emitCachedValueIfAny(subscriber);
    }

    private void emitCachedValueIfAny(Subscriber<? super T> subscriber) {
        if (cachedValue == null) {
            onEmptyCache();
        } else {
            emitLast(subscriber, cachedValue);
        }
    }

    /**
     * Override this method to handle the empty cache case. Default implementation is no-op.
     */
    protected void onEmptyCache() {
        // no op
    }

    protected void emitLast(Subscriber<? super T> subscriber, T cachedValue) {
        subscriber.onNext(cachedValue);
    }


    @Override
    public void onCompleted() {
        synchronized (subscribers) {
            while (!subscribers.isEmpty()) {
                subscribers.remove(0).onCompleted();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        cachedValue = getRecoveryValue(e);
        synchronized (subscribers) {
            while (!subscribers.isEmpty()) {
                subscribers.remove(0).onError(e);
            }
        }
    }

    /**
     * Ovverride this method to reset the cache after onError(). The default implementation
     * just clear the cached value.
     */
    protected T getRecoveryValue(Throwable e) {
        return null;
    }

    @Override
    public void onNext(T newValue) {
        this.cachedValue = newValue;
        synchronized (subscribers) {
            for (Subscriber<? super T> subscriber : subscribers) {
                subscriber.onNext(newValue);
            }
        }
    }

    public void push() {
        if (cachedValue == null) {
            return;
        }
        synchronized (subscribers) {
            for (Subscriber<? super T> subscriber : subscribers) {
                subscriber.onNext(cachedValue);
            }
        }
    }

    private class CachedSubscription implements Subscription {
        private final Subscriber<? super T> subscriber;

        public CachedSubscription(Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void unsubscribe() {
            subscribers.remove(subscriber);
        }

        @Override
        public boolean isUnsubscribed() {
            return !subscribers.contains(subscriber);
        }
    }

}
