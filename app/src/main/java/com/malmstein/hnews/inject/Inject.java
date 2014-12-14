package com.malmstein.hnews.inject;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.feed.NewsPersister;
import com.malmstein.hnews.feed.NewsProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public class Inject {

    private static Inject INSTANCE;
    private final NewsProvider newsProvider;
    private final ConnectionProvider connectionProvider;

    private Inject(NewsProvider newsProvider, ConnectionProvider connectionProvider) {
        this.newsProvider = newsProvider;
        this.connectionProvider = connectionProvider;
    }

    public static void using(DependenciesFactory factory) {
        ConnectionProvider connectionProvider = factory.createConnectionProvider();
        NewsPersister newsPersister = factory.createFeedPersister();

        NewsProvider newsProvider = factory.createFeedProvider(newsPersister, connectionProvider);
        INSTANCE = new Inject(newsProvider, connectionProvider);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static NewsProvider feedProvider() {
        return instance().newsProvider;
    }

    public static ConnectionProvider connectionProvider() {
        return instance().connectionProvider;
    }

}
