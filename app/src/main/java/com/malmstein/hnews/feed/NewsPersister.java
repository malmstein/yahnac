package com.malmstein.hnews.feed;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.malmstein.hnews.data.HNewsContract;
import com.malmstein.hnews.model.Item;

import java.util.ArrayList;
import java.util.Map;

public class NewsPersister {

    private final ContentResolver contentResolver;

    public NewsPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persistItem(Map<String, Object> itemMap){
        String type = (String) itemMap.get("type");

        if (type.equals(Item.TYPE.comment.name())){

        }

        if (type.equals(Item.TYPE.story.name())) {
            persistStory(itemMap);
        }

        if (type.equals(Item.TYPE.poll.name())) {

        }

        if (type.equals(Item.TYPE.pollopt.name())) {

        }
    }

    private void persistStory(Map<String, Object> map){

        String by = (String) map.get("by");
        Long id = (Long) map.get("id");

        ArrayList<String> kidsArray = (ArrayList<String>) map.get("kids");
        Gson gson = new Gson();
        String kids = gson.toJson(kidsArray);

        long score = (Long) map.get("score");
        long time = (long) map.get("time");
        String title = (String) map.get("title");
        String type = (String) map.get("type");
        String url = (String) map.get("url");

        ContentValues storyValues = new ContentValues();

        storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, id);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_BY, by);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_SCORE, score);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_KIDS, kids);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TIME, time);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TITLE, title);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TYPE, type);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_URL, url);

        Cursor storyCursor = contentResolver.query(
                HNewsContract.ItemEntry.CONTENT_URI,
                new String[]{HNewsContract.ItemEntry.COLUMN_BY},
                HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{id.toString()},
                null);

        if (storyCursor.moveToFirst()) {
            contentResolver.update(HNewsContract.ItemEntry.CONTENT_URI, storyValues,
                    HNewsContract.ItemEntry.COLUMN_ITEM_ID + " = ?",
                    new String[]{id.toString()});
        } else {
            contentResolver.insert(HNewsContract.ItemEntry.CONTENT_URI, storyValues);
        }
        storyCursor.close();
    }
}
