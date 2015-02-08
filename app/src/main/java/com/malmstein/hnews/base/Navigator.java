package com.malmstein.hnews.base;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.malmstein.hnews.comments.CommentFragment;
import com.malmstein.hnews.comments.CommentsActivity;
import com.malmstein.hnews.model.Story;

public class Navigator {

    private final ActionBarActivity activity;

    public Navigator(ActionBarActivity activity) {
        this.activity = activity;
    }

    public void toComments(Story story, View v) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        int[] startingLocation = new int[2];
        startingLocation[0] += v.getWidth() / 2;
        v.getLocationOnScreen(startingLocation);
        commentIntent.putExtra(CommentFragment.ARG_LOCATION, startingLocation);
        commentIntent.putExtra(CommentFragment.ARG_STORY_ID, story.getId());
        commentIntent.putExtra(CommentFragment.ARG_STORY_TITLE, story.getComments());

        ActivityCompat.startActivity(activity, commentIntent, null);
    }
}
