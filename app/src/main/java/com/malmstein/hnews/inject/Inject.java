package com.malmstein.hnews.inject;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.comments.CommentsProvider;
import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.StoriesProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public class Inject {

    private static Inject INSTANCE;
    private final StoriesProvider storiesProvider;
    private final CommentsProvider commentsProvider;
    private final ConnectionProvider connectionProvider;

    private Inject(StoriesProvider storiesProvider, CommentsProvider commentsProvider, ConnectionProvider connectionProvider) {
        this.storiesProvider = storiesProvider;
        this.commentsProvider = commentsProvider;
        this.connectionProvider = connectionProvider;
    }

    public static void using(DependenciesFactory factory) {
        ConnectionProvider connectionProvider = factory.createConnectionProvider();
        DatabasePersister databasePersister = factory.createDatabasePersister();

        StoriesProvider storiesProvider = factory.createStoriesProvider(databasePersister);
        CommentsProvider commentsProvider = factory.createCommentsProvider(databasePersister);

        INSTANCE = new Inject(storiesProvider, commentsProvider, connectionProvider);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static StoriesProvider storiesProvider() {
        return instance().storiesProvider;
    }

    public static CommentsProvider commentsProvider() { return instance().commentsProvider; }

    public static ConnectionProvider connectionProvider() {
        return instance().connectionProvider;
    }

}
