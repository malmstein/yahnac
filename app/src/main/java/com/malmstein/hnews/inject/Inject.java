package com.malmstein.hnews.inject;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.data.DataPersister;

public class Inject {

    private static Inject INSTANCE;
    private final DataRepository dataRepository;

    private Inject(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public static void using(DependenciesFactory factory) {
        DataPersister dataPersister = factory.createDatabasePersister();

        DataRepository dataRepository = factory.createDataRepository(dataPersister);

        INSTANCE = new Inject(dataRepository);
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

}
