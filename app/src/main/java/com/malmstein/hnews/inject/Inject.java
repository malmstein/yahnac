package com.malmstein.hnews.inject;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.comments.CommentsProvider;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.feed.DatabasePersister;

public class Inject {

    private static Inject INSTANCE;
    private final CommentsProvider commentsProvider;
    private final DataRepository dataRepository;

    private Inject(CommentsProvider commentsProvider, DataRepository dataRepository) {
        this.commentsProvider = commentsProvider;
        this.dataRepository = dataRepository;
    }

    public static void using(DependenciesFactory factory) {
        DatabasePersister databasePersister = factory.createDatabasePersister();

        CommentsProvider commentsProvider = factory.createCommentsProvider(databasePersister);

        DataRepository dataRepository = factory.createDataRepository(databasePersister);

        INSTANCE = new Inject(commentsProvider, dataRepository);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static CommentsProvider commentsProvider() { return instance().commentsProvider; }

    public static DataRepository dataRepository() {
        return instance().dataRepository;
    }

}
