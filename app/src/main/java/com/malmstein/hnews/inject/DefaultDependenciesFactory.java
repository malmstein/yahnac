package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.comments.CommentsProvider;
import com.malmstein.hnews.comments.CommentsRetriever;
import com.malmstein.hnews.comments.PersistedCommentsProvider;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.feed.DatabasePersister;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    public CommentsRetriever createCommentsRetriever(DatabasePersister databasePersister) {
        return new CommentsRetriever(databasePersister);
    }

    @Override
    public DatabasePersister createDatabasePersister() {
        return new DatabasePersister(context.getContentResolver());
    }

    @Override
    public CommentsProvider createCommentsProvider(DatabasePersister databasePersister) {
        return new PersistedCommentsProvider(createCommentsRetriever(databasePersister));
    }

    @Override
    public DataRepository createDataRepository(DatabasePersister databasePersister) {
        return new DataRepository(databasePersister);
    }

}
