package com.malmstein.yahnac.inject;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.DataRepository;

public interface DependenciesFactory {

    DataPersister createDatabasePersister();

    DataRepository createDataRepository(DataPersister dataPersister);

    CrashAnalytics createCrashAnalytics();

}
