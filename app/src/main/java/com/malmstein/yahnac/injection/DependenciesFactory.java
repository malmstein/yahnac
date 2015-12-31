package com.malmstein.yahnac.injection;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.analytics.UsageAnalytics;
import com.malmstein.yahnac.data.ConnectionProvider;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.Provider;
import com.malmstein.yahnac.invite.AppInviter;

public interface DependenciesFactory {

    DataPersister createDatabasePersister();

    Provider createDataRepository(DataPersister dataPersister);

    CrashAnalytics createCrashAnalytics();

    ConnectionProvider createConnection();

    UsageAnalytics createUsageAnalytics();

    AppInviter createAppInviter();

}
