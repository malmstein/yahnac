package com.malmstein.hnews.feed;

public interface StoriesProvider {

    void refresh();

    void fetchNewStories();

    void fetchBestStories();

    void fetchTopStories();

    void fetchShowStories();

    void fetchAskStories();

}