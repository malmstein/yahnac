package com.malmstein.hnews.base;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
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
        commentIntent.putExtra(CommentFragment.ARG_STORY_ID, story.getId());
        commentIntent.putExtra(CommentFragment.ARG_STORY_TITLE, story.getTitle());

        if (Build.VERSION_CODES.LOLLIPOP >= Build.VERSION.SDK_INT) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, v, "comment");
            ActivityCompat.startActivity(activity, commentIntent, options.toBundle());
        } else {
            ActivityCompat.startActivity(activity, commentIntent, null);
        }

    }
}
