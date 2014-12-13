package com.malmstein.hnews.feed;

import android.content.ContentResolver;

import com.malmstein.hnews.http.ConnectionProvider;

import rx.Observable;

public class FeedUpdateRetriever implements Retriever<FeedUpdateEvent> {

    private final ContentResolver contentResolver;
    private final ConnectionProvider connectionProvider;

    public FeedUpdateRetriever(ContentResolver contentResolver, ConnectionProvider connectionProvider) {
        this.contentResolver = contentResolver;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Observable<FeedUpdateEvent> fetch() {
        return null;
    }
}