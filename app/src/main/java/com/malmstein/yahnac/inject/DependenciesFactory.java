package com.malmstein.yahnac.inject;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.analytics.UsageAnalytics;
import com.malmstein.yahnac.data.ConnectionProvider;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.Provider;

public interface DependenciesFactory {

    DataPersister createDatabasePersister();

    Provider createDataRepository(DataPersister dataPersister);

    CrashAnalytics createCrashAnalytics();

    ConnectionProvider createConnection();

    UsageAnalytics createUsageAnalytics();

}
