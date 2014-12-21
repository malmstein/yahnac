package com.malmstein.hnews.feed;

import rx.Observable;

public interface Retriever<T> {

    Observable<T> fetchStories();
    Observable<T> fetchComments();

}
