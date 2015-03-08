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
                HNewsContract.StoryEntry.COLUMN_TIMESTAMP + " <= ?",
                new String[]{timestampTwoDaysAgo});

        ContentValues[] cvArray = new ContentValues[topStories.size()];
        topStories.toArray(cvArray);

        return contentResolver.bulkInsert(HNewsContract.StoryEntry.CONTENT_STORY_URI, cvArray);
    }

    public int persistComments(Vector<ContentValues> commentsVector, Long storyId) {
        contentResolver.delete(HNewsContract.CommentsEntry.CONTENT_COMMENTS_URI,
                HNewsContract.CommentsEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{storyId.toString()});

        ContentValues[] cvArray = new ContentValues[commentsVector.size()];
        commentsVector.toArray(cvArray);
        return contentResolver.bulkInsert(HNewsContract.CommentsEntry.CONTENT_COMMENTS_URI, cvArray);
    }

    public void onBookmarkClicked(Story story) {
        if (story.isBookmark()) {
            removeBookmark(story);
        } else {
            addBookmark(story);
        }
    }

    private void addBookmark(Story story) {
        ContentValues storyValues = new ContentValues();

        storyValues.put(HNewsContract.BookmarkEntry.ITEM_ID, story.getInternalId());
        storyValues.put(HNewsContract.BookmarkEntry.BY, story.getSubmitter());
        storyValues.put(HNewsContract.BookmarkEntry.TYPE, story.getType());
        storyValues.put(HNewsContract.BookmarkEntry.DOMAIN, story.getDomain());
        storyValues.put(HNewsContract.BookmarkEntry.URL, story.getUrl());
        storyValues.put(HNewsContract.BookmarkEntry.TITLE, story.getTitle());
        storyValues.put(HNewsContract.BookmarkEntry.TIMESTAMP, System.currentTimeMillis());

        contentResolver.insert(HNewsContract.BookmarkEntry.CONTENT_BOOKMARKS_URI, storyValues);
    }

    private void removeBookmark(Story story) {
        contentResolver.delete(HNewsContract.BookmarkEntry.CONTENT_BOOKMARKS_URI,
                HNewsContract.BookmarkEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{story.getInternalId().toString()});
    }

}
