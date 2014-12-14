package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.feed.FeedPersister;
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

    private Retriever<FeedUpdateEvent> createRetriever(FeedPersister feedPersister, ConnectionProvider connectionProvider) {
        return new FeedUpdateRetriever(
                feedPersister,
                connectionProvider);
    }

    @Override
    public FeedPersister createFeedPersister() {
        return new FeedPersister(context.getContentResolver());
    }

    @Override
    public FeedProvider createFeedProvider(FeedPersister feedPersister, ConnectionProvider connectionProvider) {
        return new PersistedFeedProvider(createRetriever(feedPersister, connectionProvider), feedPersister);
    }

    @Override
    public ConnectionProvider createConnectionProvider() {
        return AndroidConnectionProvider.newInstance();
    }

}
