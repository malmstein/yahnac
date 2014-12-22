package com.malmstein.hnews.feed;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.google.gson.Gson;
import com.malmstein.hnews.data.HNewsContract;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class DatabasePersister {

    private final ContentResolver contentResolver;

    public DatabasePersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persistStory(Map<String, Object> map, int order) {

        String by = (String) map.get("by");
        Long id = (Long) map.get("id");
        String type = (String) map.get("type");
        long time = (long) map.get("time");

        ArrayList<String> kidsArray = (ArrayList<String>) map.get("kids");
        Gson gson = new Gson();
        String kids = gson.toJson(kidsArray);

        long score = (Long) map.get("score");
        String title = (String) map.get("title");
        String url = (String) map.get("url");

        ContentValues storyValues = new ContentValues();

        storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, id);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_BY, by);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TYPE, type);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TIME, time * 1000);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_SCORE, score);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_KIDS, kids);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TITLE, title);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_URL, url);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ORDER, order);

        contentResolver.insert(HNewsContract.ItemEntry.CONTENT_STORY_TMP_URI, storyValues);
    }

    public void moveFromTmpToStories() {
        Cursor tmpStories = contentResolver.query(HNewsContract.ItemEntry.CONTENT_STORY_TMP_URI,
                HNewsContract.STORY_COLUMNS,
                null,
                null,
                null);

        ArrayList<ContentValues> tmpStoriesValues = new ArrayList<>();
        ContentValues map;
        if (tmpStories.moveToFirst()) {
            do {
                map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(tmpStories, map);
                tmpStoriesValues.add(map);
            } while (tmpStories.moveToNext());
        }
        tmpStories.close();

        ContentValues[] cvArray = new ContentValues[tmpStoriesValues.size()];
        tmpStoriesValues.toArray(cvArray);

        int stories = contentResolver.delete(HNewsContract.ItemEntry.CONTENT_STORY_URI,
                null,
                null);

        int tmp_stories = contentResolver.delete(HNewsContract.ItemEntry.CONTENT_STORY_TMP_URI,
                null,
                null);

        contentResolver.bulkInsert(HNewsContract.ItemEntry.CONTENT_STORY_URI,
                cvArray);

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
