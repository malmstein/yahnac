package com.malmstein.hnews.feed;

import android.content.ContentResolver;

import com.malmstein.hnews.model.Item;

import rx.Observable;

public class PersistedFeedProvider implements FeedProvider {

    private final Retriever<FeedUpdateEvent> retriever;
    private final ContentResolver contentResolver;

    public PersistedFeedProvider(Retriever<FeedUpdateEvent> retriever, ContentResolver contentResolver) {
        this.retriever = retriever;
        this.contentResolver = contentResolver;
    }

    @Override
    public void refresh() {

    }

    @Override
    public Observable<Item> getNewsObservable() {
        return null;
    }
}