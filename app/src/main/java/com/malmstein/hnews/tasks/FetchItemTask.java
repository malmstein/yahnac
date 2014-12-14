package com.malmstein.hnews.tasks;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.malmstein.hnews.feed.NewsPersister;

import java.util.Map;

public class FetchItemTask {

    public FetchItemTask(final NewsPersister newsPersister, Long itemId) {

        Firebase item = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + itemId);
        item.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();
                newsPersister.persistItem(newItem);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
