package com.malmstein.hnews.feed;

import rx.Observable;

public interface StoriesRetriever<T> extends Retriever {

    Observable<T> fetch(Long... params);

    Observable<T> fetchTop();

    Observable<T> fetchNew();

    Observable<T> fetchBest();

    Observable<T> fetchShow();

    Observable<T> fetchAsk();
}
