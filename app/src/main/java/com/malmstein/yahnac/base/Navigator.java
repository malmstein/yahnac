package com.malmstein.yahnac.base;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.comments.CommentsActivity;
import com.malmstein.yahnac.comments.CommentsFragment;
import com.malmstein.yahnac.connectivity.NetworkDetector;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.settings.SettingsActivity;
import com.malmstein.yahnac.stories.ArticleActivity;
import com.malmstein.yahnac.stories.BookmarksActivity;
import com.malmstein.yahnac.stories.NewsActivity;

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
            articleIntent.putExtra(ArticleActivity.ARG_STORY, story);

            ActivityCompat.startActivity(activity, articleIntent, null);
        }
    }

    public void toComments(View v, Story story) {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, new Pair<>(v, CommentsActivity.VIEW_NAME_HEADER_TITLE));

        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsFragment.ARG_STORY, story);

        ActivityCompat.startActivity(activity, commentIntent, activityOptions.toBundle());
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

    public void toNews() {
        Intent newsIntent = new Intent(activity, NewsActivity.class);
        ActivityCompat.startActivity(activity, newsIntent, null);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }

    public void toBookmarks() {
        Intent bookmarksIntent = new Intent(activity, BookmarksActivity.class);
        ActivityCompat.startActivity(activity, bookmarksIntent, null);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }
}
