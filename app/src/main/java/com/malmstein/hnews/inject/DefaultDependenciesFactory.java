package com.malmstein.hnews.inject;

import android.content.Context;

import com.malmstein.hnews.feed.ItemsRetriever;
import com.malmstein.hnews.feed.NewsPersister;
import com.malmstein.hnews.feed.NewsProvider;
import com.malmstein.hnews.feed.NewsUpdateEvent;
import com.malmstein.hnews.feed.PersistedNewsProvider;
import com.malmstein.hnews.feed.Retriever;

public class DefaultDependenciesFactory implements DependenciesFactory {

    private final Context context;

    public DefaultDependenciesFactory(Context context) {
        this.context = context;
    }

    private Retriever<NewsUpdateEvent> createRetriever(NewsPersister newsPersister) {
        return new ItemsRetriever(newsPersister);
    }

    @Override
    public NewsPersister createFeedPersister() {
        return new NewsPersister(context.getContentResolver());
    }

    @Override
    public NewsProvider createFeedProvider(NewsPersister newsPersister) {
        return new PersistedNewsProvider(createRetriever(newsPersister));
    }

}
