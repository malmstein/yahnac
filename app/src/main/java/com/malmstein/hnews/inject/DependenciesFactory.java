package com.malmstein.hnews.inject;

import com.malmstein.hnews.feed.NewsPersister;
import com.malmstein.hnews.feed.NewsProvider;

public interface DependenciesFactory {

    NewsPersister createFeedPersister();

    NewsProvider createFeedProvider(NewsPersister newsPersister);

}
