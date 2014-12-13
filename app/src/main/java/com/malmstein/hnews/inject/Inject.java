package com.malmstein.hnews.inject;

import android.content.ContentResolver;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.feed.FeedProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public class Inject {

    private static Inject INSTANCE;

    private final ContentResolver contentResolver;
    private final FeedProvider feedProvider;
    private final ConnectionProvider connectionProvider;

    private Inject(ContentResolver contentResolver, FeedProvider feedProvider, ConnectionProvider connectionProvider) {
        this.contentResolver = contentResolver;
        this.feedProvider = feedProvider;
        this.connectionProvider = connectionProvider;
    }

    public static void using(DependenciesFactory factory, ContentResolver contentResolver) {
        ConnectionProvider connectionProvider = factory.createConnectionProvider();

        FeedProvider feedProvider = factory.createFeedProvider(contentResolver, connectionProvider);
        INSTANCE = new Inject(contentResolver, feedProvider, connectionProvider);
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
