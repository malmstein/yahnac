package com.malmstein.yahnac.tasks;

import android.content.ContentValues;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.malmstein.yahnac.data.HNewsContract;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseItemTask {

    public ContentValues execute(Long itemId) {

        Firebase item = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + itemId);
        item.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();
                return persistStory(newItem);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private ContentValues persistStory(Map<String, Object> map) {
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

}
