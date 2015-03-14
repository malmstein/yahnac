package com.malmstein.yahnac.data;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.malmstein.yahnac.base.HNewsDate;
import com.malmstein.yahnac.model.Story;

import java.util.Vector;

public class DataPersister {

    private final ContentResolver contentResolver;

    public DataPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public int persistStories(Vector<ContentValues> topStories) {

        String timestampTwoDaysAgo = String.valueOf(HNewsDate.now().twoDaysAgo().getTimeInMillis());
        contentResolver.delete(HNewsContract.StoryEntry.CONTENT_STORY_URI,
                HNewsContract.StoryEntry.TIMESTAMP + " <= ?",
                new String[]{timestampTwoDaysAgo});

        ContentValues[] cvArray = new ContentValues[topStories.size()];
        topStories.toArray(cvArray);

        return contentResolver.bulkInsert(HNewsContract.StoryEntry.CONTENT_STORY_URI, cvArray);
    }

    public int persistComments(Vector<ContentValues> commentsVector, Long storyId) {
        contentResolver.delete(HNewsContract.CommentsEntry.CONTENT_COMMENTS_URI,
                HNewsContract.CommentsEntry.ITEM_ID + " = ?",
                new String[]{storyId.toString()});

        ContentValues[] cvArray = new ContentValues[commentsVector.size()];
        commentsVector.toArray(cvArray);
        return contentResolver.bulkInsert(HNewsContract.CommentsEntry.CONTENT_COMMENTS_URI, cvArray);
    }

    public void addBookmark(Story story) {
        ContentValues storyValues = new ContentValues();

        storyValues.put(HNewsContract.BookmarkEntry.ITEM_ID, story.getId());
        storyValues.put(HNewsContract.BookmarkEntry.BY, story.getSubmitter());
        storyValues.put(HNewsContract.BookmarkEntry.TYPE, story.getType());
        storyValues.put(HNewsContract.BookmarkEntry.DOMAIN, story.getDomain());
        storyValues.put(HNewsContract.BookmarkEntry.URL, story.getUrl());
        storyValues.put(HNewsContract.BookmarkEntry.TITLE, story.getTitle());
        storyValues.put(HNewsContract.BookmarkEntry.TIMESTAMP, System.currentTimeMillis());

        contentResolver.insert(HNewsContract.BookmarkEntry.CONTENT_BOOKMARKS_URI, storyValues);

        //TODO add flag to current story
    }

    public void removeBookmark(Story story) {
        contentResolver.delete(HNewsContract.BookmarkEntry.CONTENT_BOOKMARKS_URI,
                HNewsContract.BookmarkEntry.ITEM_ID + " = ?",
                new String[]{story.getId().toString()});

        //TODO remove flag to current story
    }

}
