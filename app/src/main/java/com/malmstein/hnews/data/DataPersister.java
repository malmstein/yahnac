package com.malmstein.hnews.data;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.malmstein.hnews.data.HNewsContract;

import java.util.Vector;

public class DataPersister {

    private final ContentResolver contentResolver;

    public DataPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persistStories(Vector<ContentValues> topStories) {

        ContentValues[] cvArray = new ContentValues[topStories.size()];
        topStories.toArray(cvArray);
        contentResolver.bulkInsert(HNewsContract.ItemEntry.CONTENT_STORY_URI, cvArray);
    }

    public int persistStoriesAndReturnRows(Vector<ContentValues> topStories) {
        ContentValues[] cvArray = new ContentValues[topStories.size()];
        topStories.toArray(cvArray);
        return contentResolver.bulkInsert(HNewsContract.ItemEntry.CONTENT_STORY_URI, cvArray);
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
