package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.comments.CommentsProvider;
import com.malmstein.hnews.comments.CommentsRetriever;
import com.malmstein.hnews.comments.PersistedCommentsProvider;
import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.StoriesRetriever;
import com.malmstein.hnews.feed.PersistedStoriesProvider;
import com.malmstein.hnews.feed.StoriesProvider;
import com.malmstein.hnews.http.AndroidConnectionProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    private StoriesRetriever createStoriesRetriever(DatabasePersister databasePersister) {
        return new StoriesRetriever(databasePersister);
    }

    public CommentsRetriever createCommentsRetriever(DatabasePersister databasePersister, ConnectionProvider connectionProvider) {
        return new CommentsRetriever(databasePersister, connectionProvider);
    }

    @Override
    public DatabasePersister createDatabasePersister() {
        return new DatabasePersister(context.getContentResolver());
    }

    @Override
    public StoriesProvider createStoriesProvider(DatabasePersister databasePersister) {
        return new PersistedStoriesProvider(createStoriesRetriever(databasePersister));
    }

    @Override
    public CommentsProvider createCommentsProvider(DatabasePersister databasePersister) {
        return new PersistedCommentsProvider(createCommentsRetriever(databasePersister, createConnectionProvider()));
    }

    @Override
    public ConnectionProvider createConnectionProvider() {
        return AndroidConnectionProvider.newInstance();
    }

}
