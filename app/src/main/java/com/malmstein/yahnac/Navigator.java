package com.malmstein.yahnac;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.malmstein.yahnac.bookmarks.BookmarksActivity;
import com.malmstein.yahnac.comments.CommentsActivity;
import com.malmstein.yahnac.comments.CommentsPresenter;
import com.malmstein.yahnac.login.LoginActivity;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.settings.SettingsActivity;
import com.malmstein.yahnac.stories.NewsActivity;
import com.malmstein.yahnac.story.CustomTabActivityHelper;
import com.malmstein.yahnac.story.StoryActivity;
import com.malmstein.yahnac.views.transitions.TransitionHelper;

public class Navigator {

    private final HNewsActivity activity;
    private CustomTabActivityHelper customTabActivityHelper;

    public Navigator(HNewsActivity activity) {
        this.activity = activity;
        customTabActivityHelper = new CustomTabActivityHelper();
    }

    public void onStart() {
        customTabActivityHelper.bindCustomTabsService(activity);
    }

    public void onStop() {
        customTabActivityHelper.unbindCustomTabsService(activity);
    }

    protected boolean isOnline() {
        return activity.isOnline();
    }

    public void toExternalBrowser(Uri articleUri) {
        if (isOnline()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(articleUri);

            ActivityCompat.startActivity(activity, browserIntent, null);
        }
    }

    public void toInnerBrowser(final Story story) {
        if (isOnline()) {

            customTabActivityHelper.mayLaunchUrl(Uri.parse(story.getUrl()), null, null);
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

            int color = activity.getResources().getColor(R.color.orange);
            intentBuilder.setToolbarColor(color);

            CustomTabActivityHelper.openCustomTab(
                    activity, intentBuilder.build(),
                    Uri.parse(story.getUrl()), new CustomTabActivityHelper.CustomTabFallback() {
                        @Override
                        public void openUri(Activity activity, Uri uri) {
                            Intent articleIntent = new Intent(activity, StoryActivity.class);
                            articleIntent.putExtra(StoryActivity.ARG_STORY, story);
                            ActivityCompat.startActivity(activity, articleIntent, null);
                        }
                    });

        }
    }

    public void toComments(View v, Story story) {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, new Pair<>(v, CommentsPresenter.VIEW_NAME_HEADER_TITLE));

        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsActivity.ARG_STORY, story);

        ActivityCompat.startActivity(activity, commentIntent, activityOptions.toBundle());
    }

    public void toComments(Story story) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsActivity.ARG_STORY, story);

        ActivityCompat.startActivity(activity, commentIntent, null);
    }

    public void toSettings() {
        Intent settingsIntent = new Intent(activity, SettingsActivity.class);
        ActivityCompat.startActivity(activity, settingsIntent, null);
    }

    public void toNews() {
        Intent newsIntent = new Intent(activity, NewsActivity.class);
        newsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public void toLogin(View v) {

        final android.util.Pair[] pairs =
                TransitionHelper.createSafeTransitionParticipants
                        (activity,
                                false,
                                new android.util.Pair<>(v, LoginActivity.VIEW_TOOLBAR_TITLE));

        if ((v != null) && (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)) {
            ActivityOptions sceneTransitionAnimation = ActivityOptions
                    .makeSceneTransitionAnimation(activity, pairs);

            final Bundle transitionBundle = sceneTransitionAnimation.toBundle();
            Intent loginLollipopIntent = new Intent(activity, LoginActivity.class);
            ActivityCompat.startActivity(activity, loginLollipopIntent, transitionBundle);
        } else {
            Intent loginIntent = new Intent(activity, LoginActivity.class);
            ActivityCompat.startActivity(activity, loginIntent, null);
            activity.overridePendingTransition(0, 0);
        }

    }

}
