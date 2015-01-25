package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.feed.DatabasePersister;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    @Override
    public DatabasePersister createDatabasePersister() {
        return new DatabasePersister(context.getContentResolver());
    }

    @Override
    public DataRepository createDataRepository(DatabasePersister databasePersister) {
        return new DataRepository(databasePersister);
    }

}
