package com.malmstein.hnews.tasks;

import android.content.ContentValues;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.malmstein.hnews.data.HNewsContract;
import com.malmstein.hnews.feed.DatabasePersister;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class FetchTopStoriesTask {

    private final DatabasePersister databasePersister;
    private Vector<ContentValues> topStories = new Vector<>();
    private int order;

    public FetchTopStoriesTask(final DatabasePersister databasePersister) {
        this.databasePersister = databasePersister;
        Firebase topStories = new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
        topStories.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Long itemId = (Long) snapshot.getValue();
                fetchItem(itemId);
                order++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    private void fetchItem(Long itemId){

        Firebase item = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + itemId);
        item.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();
                topStories.add(transform(newItem));
                if (topStories.size() == 100){
                    databasePersister.persistStories(topStories);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private ContentValues transform(Map<String, Object> map){
        String by = (String) map.get("by");
        Long id = (Long) map.get("id");
        String type = (String) map.get("type");
        long time = (long) map.get("time");

        ArrayList<String> kidsArray = (ArrayList<String>) map.get("kids");
        Gson gson = new Gson();
        String kids = gson.toJson(kidsArray);

        long score = 0;
        if (map.containsKey("score")){
           score = (Long) map.get("score");
        }
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

        return storyValues;
    }
}
