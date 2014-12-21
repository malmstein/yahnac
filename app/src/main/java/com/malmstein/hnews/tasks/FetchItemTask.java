package com.malmstein.hnews.tasks;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.malmstein.hnews.feed.DatabasePersister;

import java.util.Map;

public class FetchItemTask {

    public FetchItemTask(final DatabasePersister databasePersister, Long itemId) {

        Firebase item = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + itemId);
        item.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();
                databasePersister.persistItem(newItem);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
