package com.malmstein.hnews.inject;

import com.malmstein.hnews.analytics.CrashAnalytics;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.connectivity.WizMerlin;
import com.malmstein.hnews.data.DataPersister;
import com.malmstein.hnews.data.DataRepository;

public class Inject {

    private static Inject INSTANCE;
    private final DataRepository dataRepository;
    private final CrashAnalytics crashAnalytics;
    private final WizMerlin merlin;

    private Inject(DataRepository dataRepository, CrashAnalytics crashAnalytics, WizMerlin merlin) {
        this.dataRepository = dataRepository;
        this.crashAnalytics = crashAnalytics;
        this.merlin = merlin;
    }

    public static void using(DependenciesFactory factory) {
        DataPersister dataPersister = factory.createDatabasePersister();

        DataRepository dataRepository = factory.createDataRepository(dataPersister);
        CrashAnalytics crashAnalytics = factory.createCrashAnalytics();
        WizMerlin merlin = factory.createWizMerlin();

        INSTANCE = new Inject(dataRepository, crashAnalytics, merlin);
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

    public static WizMerlin merlin() {
        return instance().merlin;
    }

}
