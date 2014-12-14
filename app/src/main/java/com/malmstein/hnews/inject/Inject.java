package com.malmstein.hnews.inject;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.feed.FeedPersister;
import com.malmstein.hnews.feed.FeedProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public class Inject {

    private static Inject INSTANCE;
    private final FeedProvider feedProvider;
    private final ConnectionProvider connectionProvider;

    private Inject(FeedProvider feedProvider, ConnectionProvider connectionProvider) {
        this.feedProvider = feedProvider;
        this.connectionProvider = connectionProvider;
    }

    public static void using(DependenciesFactory factory) {
        ConnectionProvider connectionProvider = factory.createConnectionProvider();
        FeedPersister feedPersister = factory.createFeedPersister();

        FeedProvider feedProvider = factory.createFeedProvider(feedPersister, connectionProvider);
        INSTANCE = new Inject(feedProvider, connectionProvider);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static FeedProvider feedProvider() {
        return instance().feedProvider;
    }

    public static ConnectionProvider connectionProvider() {
        return instance().connectionProvider;
    }

}
