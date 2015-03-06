package com.malmstein.hnews.base;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;

import com.malmstein.hnews.comments.CommentsFragment;
import com.malmstein.hnews.comments.CommentsActivity;
import com.malmstein.hnews.connectivity.WizMerlin;
import com.malmstein.hnews.inject.Inject;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.stories.ArticleActivity;
import com.malmstein.hnews.stories.ArticleFragment;

public class Navigator {

    private final ActionBarActivity activity;
    private final WizMerlin merlin;

    public Navigator(ActionBarActivity activity) {
        this.activity = activity;
        this.merlin = Inject.merlin();
    }

    public void toExternalBrowser(Uri articleUri) {
        if (merlin.detectsWorkingNetworkConnection()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(articleUri);

            ActivityCompat.startActivity(activity, browserIntent, null);
        }
    }

    public void toInnerBrowser(Story story) {
        if (merlin.detectsWorkingNetworkConnection()) {
            Intent articleIntent = new Intent(activity, ArticleActivity.class);
            articleIntent.putExtra(ArticleFragment.ARG_STORY_ID, story.getInternalId());
            articleIntent.putExtra(ArticleFragment.ARG_STORY_TITLE, story.getTitle());

            ActivityCompat.startActivity(activity, articleIntent, null);
        }
    }

    public void toComments(Story story) {
        if (merlin.detectsWorkingNetworkConnection()) {
            Intent commentIntent = new Intent(activity, CommentsActivity.class);
            commentIntent.putExtra(CommentsFragment.ARG_STORY, story);

            ActivityCompat.startActivity(activity, commentIntent, null);
        }

    }

}
