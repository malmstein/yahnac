package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.feed.NewsPersister;
import com.malmstein.hnews.feed.NewsProvider;
import com.malmstein.hnews.feed.NewsUpdateEvent;
import com.malmstein.hnews.feed.NewsUpdateRetriever;
import com.malmstein.hnews.feed.PersistedNewsProvider;
import com.malmstein.hnews.feed.Retriever;
import com.malmstein.hnews.http.AndroidConnectionProvider;
import com.malmstein.hnews.http.ConnectionProvider;

import java.util.concurrent.TimeUnit;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private static final long MILLIS_IN_AMINUTE = TimeUnit.MINUTES.toMillis(1);

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    private Retriever<NewsUpdateEvent> createRetriever(NewsPersister newsPersister, ConnectionProvider connectionProvider) {
        return new NewsUpdateRetriever(
                newsPersister,
                connectionProvider);
    }

    @Override
    public NewsPersister createFeedPersister() {
        return new NewsPersister(context.getContentResolver());
    }

    @Override
    public NewsProvider createFeedProvider(NewsPersister newsPersister, ConnectionProvider connectionProvider) {
        return new PersistedNewsProvider(createRetriever(newsPersister, connectionProvider), newsPersister);
    }

    @Override
    public ConnectionProvider createConnectionProvider() {
        return AndroidConnectionProvider.newInstance();
    }

}
