package com.malmstein.hnews.tasks;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.malmstein.hnews.feed.NewsPersister;

public class FetchTopStoriesTask {

    public FetchTopStoriesTask(final NewsPersister newsPersister) {
        Firebase topStories = new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
        topStories.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                Long itemId = (Long) snapshot.getValue();
                new FetchItemTask(newsPersister, itemId);
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
