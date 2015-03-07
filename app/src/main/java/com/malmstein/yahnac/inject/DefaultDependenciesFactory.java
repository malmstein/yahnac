package com.malmstein.yahnac.inject;

import android.content.Context;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.analytics.CrashlyticsAnalytics;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.DataRepository;

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
    public DataRepository createDataRepository(DataPersister dataPersister) {
        return new DataRepository(dataPersister);
    }

    @Override
    public CrashAnalytics createCrashAnalytics() {
        return new CrashlyticsAnalytics();
    }

}
