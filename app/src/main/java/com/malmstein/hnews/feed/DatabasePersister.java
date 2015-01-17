package com.malmstein.hnews.feed;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.malmstein.hnews.data.HNewsContract;

import java.util.Vector;

public class DatabasePersister {

    private final ContentResolver contentResolver;

    public DatabasePersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persistStories(Vector<ContentValues> topStories) {

        ContentValues[] cvArray = new ContentValues[topStories.size()];
        topStories.toArray(cvArray);
        contentResolver.bulkInsert(HNewsContract.ItemEntry.CONTENT_STORY_TMP_URI, cvArray);
    }

    public void persistComments(Vector<ContentValues> commentsVector, Long storyId) {
        contentResolver.delete(HNewsContract.ItemEntry.CONTENT_COMMENTS_URI,
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{storyId.toString()});

        ContentValues[] cvArray = new ContentValues[commentsVector.size()];
        commentsVector.toArray(cvArray);
        contentResolver.bulkInsert(HNewsContract.ItemEntry.CONTENT_COMMENTS_URI, cvArray);
    }


}
