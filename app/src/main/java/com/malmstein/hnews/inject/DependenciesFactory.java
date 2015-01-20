package com.malmstein.hnews.inject;

import com.malmstein.hnews.comments.CommentsProvider;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.StoriesProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public interface DependenciesFactory {

    DatabasePersister createDatabasePersister();

    ConnectionProvider createConnectionProvider();

    StoriesProvider createStoriesProvider(DatabasePersister databasePersister);

    CommentsProvider createCommentsProvider(DatabasePersister databasePersister);

    DataRepository createDataRepository(DatabasePersister databasePersister);

}
