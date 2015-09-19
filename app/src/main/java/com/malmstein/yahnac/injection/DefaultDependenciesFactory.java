package com.malmstein.yahnac.injection;

import android.content.Context;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.analytics.CrashlyticsAnalytics;
import com.malmstein.yahnac.analytics.UsageAnalytics;
import com.malmstein.yahnac.data.ConnectionProvider;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.Provider;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    @Override
    public DataPersister createDatabasePersister() {
        return new DataPersister(context.getContentResolver());
    }

    @Override
    public Provider createDataRepository(DataPersister dataPersister) {
        return new Provider(dataPersister);
    }

    @Override
    public CrashAnalytics createCrashAnalytics() {
        return new CrashlyticsAnalytics();
    }

    @Override
    public ConnectionProvider createConnection() {
        return new ConnectionProvider();
    }

    @Override
    public UsageAnalytics createUsageAnalytics() {
        return new UsageAnalytics();
    }

}
