package com.malmstein.hnews.feed;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.malmstein.hnews.data.HNewsContract;
import com.malmstein.hnews.model.Item;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class DatabasePersister {

    private final ContentResolver contentResolver;

    public DatabasePersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persistItem(Map<String, Object> itemMap, int order) {
        String type = (String) itemMap.get("type");

        if (type.equals(Item.TYPE.story.name())) {
            persistStory(itemMap, order);
        }
    }

    private void persistStory(Map<String, Object> map, int order) {

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

        persistItem(storyValues, id);
    }

    private void persistItem(ContentValues contentValues, Long itemId) {
        Cursor storyCursor = contentResolver.query(
                HNewsContract.ItemEntry.CONTENT_STORY_URI,
                new String[]{HNewsContract.ItemEntry.COLUMN_BY},
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{itemId.toString()},
                null);

        if (storyCursor.moveToFirst()) {
            contentResolver.delete(HNewsContract.ItemEntry.CONTENT_STORY_URI,
                    HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                    new String[]{itemId.toString()});

        }

        contentResolver.insert(HNewsContract.ItemEntry.CONTENT_STORY_URI, contentValues);
        storyCursor.close();
    }

    public void persistComments(Vector<ContentValues> commentsVector, Long storyId) {
        contentResolver.delete(HNewsContract.ItemEntry.CONTENT_COMMENTS_URI,
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{storyId.toString()});


        ContentValues[] cvArray = new ContentValues[commentsVector.size()];
        commentsVector.toArray(cvArray);
        contentResolver.bulkInsert(HNewsContract.ItemEntry.CONTENT_COMMENTS_URI, cvArray);

//        Calendar cal = Calendar.getInstance(); //Get's a calendar object with the current time.
//        cal.add(Calendar.DATE, -1); //Signifies yesterday's date
//        String yesterdayDate = WeatherContract.getDbDateString(cal.getTime());
//        getContext().getContentResolver().delete(WeatherEntry.CONTENT_URI,
//                WeatherEntry.COLUMN_DATETEXT + " <= ?",
//                new String[]{yesterdayDate});

    }

}
