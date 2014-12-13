package com.malmstein.hnews.inject;

import android.content.ContentResolver;
import android.content.Context;

import com.malmstein.hnews.feed.FeedProvider;
import com.malmstein.hnews.feed.FeedUpdateEvent;
import com.malmstein.hnews.feed.FeedUpdateRetriever;
import com.malmstein.hnews.feed.PersistedFeedProvider;
import com.malmstein.hnews.feed.Retriever;
import com.malmstein.hnews.http.AndroidConnectionProvider;
import com.malmstein.hnews.http.ConnectionProvider;

import java.util.concurrent.TimeUnit;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private static final long MILLIS_IN_AMINUTE = TimeUnit.MINUTES.toMillis(1);

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }


    @Override
    public FeedProvider createFeedProvider(ContentResolver contentResolver, ConnectionProvider connectionProvider) {
        return new PersistedFeedProvider(createRetriever(contentResolver, connectionProvider), contentResolver);
    }

    private Retriever<FeedUpdateEvent> createRetriever(ContentResolver contentResolver, ConnectionProvider connectionProvider) {
        return new FeedUpdateRetriever(
                contentResolver,
                connectionProvider);
    }

    @Override
    public ConnectionProvider createConnectionProvider() {
        return AndroidConnectionProvider.newInstance();
    }

}
