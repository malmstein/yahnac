package com.malmstein.yahnac.inject;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.analytics.CrashlyticsAnalytics;
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
    public Tracker createUsageAnalytics() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        return analytics.newTracker(R.xml.global_tracker);
    }

}
