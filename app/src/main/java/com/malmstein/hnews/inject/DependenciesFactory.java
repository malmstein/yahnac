package com.malmstein.hnews.inject;

import com.malmstein.hnews.comments.CommentsProvider;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.feed.DatabasePersister;

public interface DependenciesFactory {

    DatabasePersister createDatabasePersister();

    CommentsProvider createCommentsProvider(DatabasePersister databasePersister);

    DataRepository createDataRepository(DatabasePersister databasePersister);

}
