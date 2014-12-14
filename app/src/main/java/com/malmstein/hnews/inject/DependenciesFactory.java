package com.malmstein.hnews.inject;

import com.malmstein.hnews.feed.FeedPersister;
import com.malmstein.hnews.feed.FeedProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public interface DependenciesFactory {

    FeedPersister createFeedPersister();

    FeedProvider createFeedProvider(FeedPersister feedPersister, ConnectionProvider connectionProvider);

    ConnectionProvider createConnectionProvider();

}
