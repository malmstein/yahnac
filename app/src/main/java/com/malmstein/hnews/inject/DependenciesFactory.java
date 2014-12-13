package com.malmstein.hnews.inject;

import android.content.ContentResolver;

import com.malmstein.hnews.feed.FeedProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public interface DependenciesFactory {

    FeedProvider createFeedProvider(ContentResolver contentResolver, ConnectionProvider connectionProvider);

    ConnectionProvider createConnectionProvider();

}
