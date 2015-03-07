package com.malmstein.yahnac.inject;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.base.DeveloperError;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.DataRepository;

public class Inject {

    private static Inject INSTANCE;
    private final DataRepository dataRepository;
    private final CrashAnalytics crashAnalytics;

    private Inject(DataRepository dataRepository, CrashAnalytics crashAnalytics) {
        this.dataRepository = dataRepository;
        this.crashAnalytics = crashAnalytics;
    }

    public static void using(DependenciesFactory factory) {
        DataPersister dataPersister = factory.createDatabasePersister();

        DataRepository dataRepository = factory.createDataRepository(dataPersister);
        CrashAnalytics crashAnalytics = factory.createCrashAnalytics();

        INSTANCE = new Inject(dataRepository, crashAnalytics);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static DataRepository dataRepository() {
        return instance().dataRepository;
    }

    public static CrashAnalytics crashAnalytics() {
        return instance().crashAnalytics;
    }

}
