package com.malmstein.yahnac.inject;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.base.DeveloperError;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.Provider;

public class Inject {

    private static Inject INSTANCE;
    private final Provider provider;
    private final CrashAnalytics crashAnalytics;
    private final DataPersister persister;

    private Inject(Provider provider, CrashAnalytics crashAnalytics, DataPersister persister) {
        this.provider = provider;
        this.crashAnalytics = crashAnalytics;
        this.persister = persister;
    }

    public static void using(DependenciesFactory factory) {
        DataPersister dataPersister = factory.createDatabasePersister();
        Provider provider = factory.createDataRepository(dataPersister);
        CrashAnalytics crashAnalytics = factory.createCrashAnalytics();

        INSTANCE = new Inject(provider, crashAnalytics, dataPersister);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static Provider provider() {
        return instance().provider;
    }

    public static CrashAnalytics crashAnalytics() {
        return instance().crashAnalytics;
    }

    public static DataPersister dataPersister() {
        return instance().persister;
    }

}
