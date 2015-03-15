package com.malmstein.yahnac.tasks;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.model.Story;

public class FirebaseTopStoriesTask {

    Firebase topStories = new Firebase("https://hacker-news.firebaseio.com/v0/topstories");



    public Story execute(String itemId) {
        Firebase topStories = new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
        topStories.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Long itemId = (Long) snapshot.getValue();
                return new FirebaseItemTask().execute(itemId);
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

}
