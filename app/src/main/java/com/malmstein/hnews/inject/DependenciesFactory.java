package com.malmstein.hnews.inject;

import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.data.DataPersister;

public interface DependenciesFactory {

    DataPersister createDatabasePersister();

    DataRepository createDataRepository(DataPersister dataPersister);

}
