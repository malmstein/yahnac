package com.malmstein.hnews.feed;

import android.content.ContentResolver;

public class FeedPersister {

    private final ContentResolver contentResolver;

    public FeedPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
}
