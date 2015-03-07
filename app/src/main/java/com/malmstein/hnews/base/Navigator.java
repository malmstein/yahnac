package com.malmstein.hnews.base;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.comments.CommentsActivity;
import com.malmstein.hnews.comments.CommentsFragment;
import com.malmstein.hnews.connectivity.NetworkDetector;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.settings.SettingsActivity;
import com.malmstein.hnews.stories.ArticleActivity;
import com.malmstein.hnews.stories.ArticleFragment;

public class Navigator {

    private final HNewsActivity activity;

    public Navigator(HNewsActivity activity) {
        this.activity = activity;
    }

    protected boolean isOnline() {
        NetworkDetector networkDetector = new NetworkDetector(activity);
        return networkDetector.isDataConnectionAvailable();
    }

    public void toExternalBrowser(Uri articleUri) {
        if (isOnline()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(articleUri);

            ActivityCompat.startActivity(activity, browserIntent, null);
        }
    }

    public void toInnerBrowser(Story story) {
        if (isOnline()) {
            Intent articleIntent = new Intent(activity, ArticleActivity.class);
            articleIntent.putExtra(ArticleFragment.ARG_STORY, story);

            ActivityCompat.startActivity(activity, articleIntent, null);
        }
    }

    public void toComments(Story story) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsFragment.ARG_STORY, story);

        ActivityCompat.startActivity(activity, commentIntent, null);
    }

    public void toSettings() {
        Intent settingsIntent = new Intent(activity, SettingsActivity.class);
        ActivityCompat.startActivity(activity, settingsIntent, null);
    }
}
