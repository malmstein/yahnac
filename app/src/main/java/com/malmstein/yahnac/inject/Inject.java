package com.malmstein.yahnac.inject;

import com.malmstein.yahnac.analytics.CrashAnalytics;
import com.malmstein.yahnac.analytics.UsageAnalytics;
import com.malmstein.yahnac.data.ConnectionProvider;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.data.Provider;
import com.novoda.notils.exception.DeveloperError;

public class Inject {

    private static Inject INSTANCE;
    private final Provider provider;
    private final CrashAnalytics crashAnalytics;
    private final DataPersister persister;
    private final ConnectionProvider connectionProvider;
    private final UsageAnalytics analyticsTracker;

    private Inject(Provider provider, CrashAnalytics crashAnalytics, DataPersister persister, ConnectionProvider connectionProvider, UsageAnalytics analyticsTracker) {
        this.provider = provider;
        this.crashAnalytics = crashAnalytics;
        this.persister = persister;
        this.connectionProvider = connectionProvider;
        this.analyticsTracker = analyticsTracker;
    }

    public static void using(DependenciesFactory factory) {
        DataPersister dataPersister = factory.createDatabasePersister();
        Provider provider = factory.createDataRepository(dataPersister);
        CrashAnalytics crashAnalytics = factory.createCrashAnalytics();
        ConnectionProvider connectionProvider = factory.createConnection();
        UsageAnalytics analyticsTracker = factory.createUsageAnalytics();
        INSTANCE = new Inject(provider, crashAnalytics, dataPersister, connectionProvider, analyticsTracker);
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

    public static ConnectionProvider connectionProvider() {
        return instance().connectionProvider;
    }

    public static UsageAnalytics usageAnalytics() {
        return instance().analyticsTracker;
    }

}
