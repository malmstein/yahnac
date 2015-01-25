package com.malmstein.hnews.inject;

import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.feed.DatabasePersister;

public interface DependenciesFactory {

    DatabasePersister createDatabasePersister();

    DataRepository createDataRepository(DatabasePersister databasePersister);

}
