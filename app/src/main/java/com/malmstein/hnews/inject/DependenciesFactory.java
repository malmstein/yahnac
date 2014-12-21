package com.malmstein.hnews.inject;

import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.HNRetriever;
import com.malmstein.hnews.feed.StoriesProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public interface DependenciesFactory {

    DatabasePersister createDatabasePersister();

    StoriesProvider createStoriesProvider(DatabasePersister databasePersister, ConnectionProvider connectionProvider);

    HNRetriever createCommentsRetriever(DatabasePersister commentsPersister, ConnectionProvider connectionProvider);

    ConnectionProvider createConnectionProvider();

}
