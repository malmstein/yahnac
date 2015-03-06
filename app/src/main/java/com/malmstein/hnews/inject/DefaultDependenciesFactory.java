package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.analytics.CrashAnalytics;
import com.malmstein.hnews.analytics.CrashlyticsAnalytics;
import com.malmstein.hnews.connectivity.WizMerlin;
import com.malmstein.hnews.data.DataPersister;
import com.malmstein.hnews.data.DataRepository;

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
        return new DataRepository(dataPersister, createWizMerlin());
    }

    @Override
    public CrashAnalytics createCrashAnalytics() {
        return new CrashlyticsAnalytics();
    }

    @Override
    public WizMerlin createWizMerlin() {
        return WizMerlin.newInstance(context);
    }

}
