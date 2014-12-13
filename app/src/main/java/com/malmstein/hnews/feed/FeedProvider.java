package com.malmstein.hnews.feed;

import com.malmstein.hnews.model.Item;

import rx.Observable;

public interface FeedProvider {

    void refresh();

    Observable<Item> getNewsObservable();
}