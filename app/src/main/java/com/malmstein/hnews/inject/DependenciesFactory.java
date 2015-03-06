package com.malmstein.hnews.inject;

import com.malmstein.hnews.analytics.CrashAnalytics;
import com.malmstein.hnews.connectivity.WizMerlin;
import com.malmstein.hnews.data.DataPersister;
import com.malmstein.hnews.data.DataRepository;

public interface DependenciesFactory {

    DataPersister createDatabasePersister();

    DataRepository createDataRepository(DataPersister dataPersister);

    CrashAnalytics createCrashAnalytics();

    WizMerlin createWizMerlin();

}
