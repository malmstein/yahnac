package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.HNRetriever;
import com.malmstein.hnews.feed.PersistedStoriesProvider;
import com.malmstein.hnews.feed.StoriesProvider;
import com.malmstein.hnews.http.AndroidConnectionProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    private HNRetriever createStoriesRetriever(DatabasePersister databasePersister, ConnectionProvider connectionProvider) {
        return new HNRetriever(databasePersister, connectionProvider);
    }

    @Override
    public DatabasePersister createDatabasePersister() {
        return new DatabasePersister(context.getContentResolver());
    }

    @Override
    public StoriesProvider createStoriesProvider(DatabasePersister databasePersister, ConnectionProvider connectionProvider) {
        return new PersistedStoriesProvider(createStoriesRetriever(databasePersister, connectionProvider));
    }

    @Override
    public HNRetriever createCommentsRetriever(DatabasePersister databasePersister, ConnectionProvider connectionProvider) {
        return createStoriesRetriever(databasePersister, connectionProvider);
    }

    @Override
    public ConnectionProvider createConnectionProvider() {
        return AndroidConnectionProvider.newInstance();
    }

}
