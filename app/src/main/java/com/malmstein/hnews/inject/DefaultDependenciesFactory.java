package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.data.DataPersister;

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

}
