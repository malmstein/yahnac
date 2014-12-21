package com.malmstein.hnews.tasks;

import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.http.ConnectionProvider;

import java.net.HttpURLConnection;

public class FetchCommentsTask {

    private final DatabasePersister databasePersister;
    private final ConnectionProvider connectionProvider;
    private final Long storyId;

    public FetchCommentsTask(final DatabasePersister databasePersister, ConnectionProvider connectionProvider, Long storyId) {

        this.databasePersister = databasePersister;
        this.connectionProvider = connectionProvider;
        this.storyId = storyId;
    }

    public void execute(){
        HttpURLConnection connection = connectionProvider.open(uri.toURL());
    }
}
