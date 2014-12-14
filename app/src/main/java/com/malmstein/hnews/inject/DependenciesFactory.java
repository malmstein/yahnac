package com.malmstein.hnews.inject;

import com.malmstein.hnews.feed.NewsPersister;
import com.malmstein.hnews.feed.NewsProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public interface DependenciesFactory {

    NewsPersister createFeedPersister();

    NewsProvider createFeedProvider(NewsPersister newsPersister, ConnectionProvider connectionProvider);

    ConnectionProvider createConnectionProvider();

}
