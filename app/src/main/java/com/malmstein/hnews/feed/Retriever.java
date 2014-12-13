package com.malmstein.hnews.feed;

import rx.Observable;

public interface Retriever<T> {

    Observable<T> fetch();

    class NoAccountSelected extends Exception {
    }

    class NetworkFailureException extends Exception {
        public NetworkFailureException(Throwable throwable) {
            super(throwable);
        }
    }

    class ReLoginNeededException extends Exception {
    }

}
