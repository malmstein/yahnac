package com.malmstein.yahnac.data;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.malmstein.yahnac.model.HNewsDate;
import com.malmstein.yahnac.model.Story;

import java.util.List;
import java.util.Vector;

public class DataPersister {

    private final ContentResolver contentResolver;

    public DataPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public int persistStories(List<ContentValues> topStories) {

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
        ContentValues bookmarkValues = new ContentValues();

        bookmarkValues.put(HNewsContract.BookmarkEntry.ITEM_ID, story.getId());
        bookmarkValues.put(HNewsContract.BookmarkEntry.BY, story.getSubmitter());
        bookmarkValues.put(HNewsContract.BookmarkEntry.TYPE, story.getType());
        bookmarkValues.put(HNewsContract.BookmarkEntry.URL, story.getUrl());
        bookmarkValues.put(HNewsContract.BookmarkEntry.TITLE, story.getTitle());
        bookmarkValues.put(HNewsContract.BookmarkEntry.TIMESTAMP, System.currentTimeMillis());
        bookmarkValues.put(HNewsContract.BookmarkEntry.FILTER, story.getFilter());

        contentResolver.insert(HNewsContract.BookmarkEntry.CONTENT_BOOKMARKS_URI, bookmarkValues);

        ContentValues storyValues = new ContentValues();
        storyValues.put(HNewsContract.StoryEntry.BOOKMARK, HNewsContract.TRUE_BOOLEAN);

        contentResolver.update(HNewsContract.StoryEntry.CONTENT_STORY_URI,
                storyValues,
                HNewsContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});
    }

    public void removeBookmark(Story story) {
        contentResolver.delete(HNewsContract.BookmarkEntry.CONTENT_BOOKMARKS_URI,
                HNewsContract.BookmarkEntry.ITEM_ID + " = ?",
                new String[]{story.getId().toString()});

        ContentValues storyValues = new ContentValues();
        storyValues.put(HNewsContract.StoryEntry.BOOKMARK, HNewsContract.FALSE_BOOLEAN);

        contentResolver.update(HNewsContract.StoryEntry.CONTENT_STORY_URI,
                storyValues,
                HNewsContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});
    }

    public void markStoryAsRead(Story story) {
        ContentValues bookmarkValues = new ContentValues();

        bookmarkValues.put(HNewsContract.StoryEntry.READ, HNewsContract.TRUE_BOOLEAN);

        contentResolver.update(HNewsContract.StoryEntry.CONTENT_STORY_URI,
                bookmarkValues,
                HNewsContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});

    }

    public void addVote(Story story) {
        ContentValues bookmarkValues = new ContentValues();

        bookmarkValues.put(HNewsContract.StoryEntry.VOTED, HNewsContract.TRUE_BOOLEAN);

        contentResolver.update(HNewsContract.StoryEntry.CONTENT_STORY_URI,
                bookmarkValues,
                HNewsContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(story.getId())});
    }
}
