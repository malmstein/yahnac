package com.malmstein.hnews.base;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;

import com.malmstein.hnews.comments.CommentFragment;
import com.malmstein.hnews.comments.CommentsActivity;
import com.malmstein.hnews.model.Story;

public class Navigator {

    private final ActionBarActivity activity;

    public Navigator(ActionBarActivity activity) {
        this.activity = activity;
    }

    public void toComments(Story story) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentFragment.ARG_STORY_ID, story.getId());
        commentIntent.putExtra(CommentFragment.ARG_STORY_COMMENTS, story.getComments());

        ActivityCompat.startActivity(activity, commentIntent, null);
    }
}
