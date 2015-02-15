package com.malmstein.hnews.base;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;

import com.malmstein.hnews.comments.CommentFragment;
import com.malmstein.hnews.comments.CommentsActivity;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.stories.ArticleActivity;
import com.malmstein.hnews.stories.ArticleFragment;

public class Navigator {

    private final ActionBarActivity activity;

    public Navigator(ActionBarActivity activity) {
        this.activity = activity;
    }

    public void toExternalBrowser(Uri articleUri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(articleUri);

        ActivityCompat.startActivity(activity, browserIntent, null);
    }

    public void toInnerBrowser(Story story) {
        Intent articleIntent = new Intent(activity, ArticleActivity.class);
        articleIntent.putExtra(ArticleFragment.ARG_STORY_ID, story.getInternalId());
        articleIntent.putExtra(ArticleFragment.ARG_STORY_TITLE, story.getTitle());

        ActivityCompat.startActivity(activity, articleIntent, null);
    }

    public void toComments(Story story) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentFragment.ARG_STORY_ID, story.getId());
        commentIntent.putExtra(CommentFragment.ARG_STORY_COMMENTS, story.getComments());

        ActivityCompat.startActivity(activity, commentIntent, null);
    }

}
