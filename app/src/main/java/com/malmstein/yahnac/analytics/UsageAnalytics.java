package com.malmstein.yahnac.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.model.Story;

public class UsageAnalytics {

    private Tracker analyticsTracker;

    private boolean isActive() {
        return BuildConfig.ENABLE_USAGE_ANALYTICS;
    }

    public void initTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analyticsTracker = analytics.newTracker(R.xml.global_tracker);
    }

    public void trackPage(String page) {
        if (isActive()) {
            analyticsTracker.setScreenName(page);
            analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public void trackStory(String page, Story story) {
        if (isActive()) {
            analyticsTracker.setScreenName(page + ": " + story.getId());
            analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public void trackEvent(String action) {
        if (isActive()) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction(action)
                    .build());
        }
    }

    public void trackNavigateEvent(String action, Story story) {
        if (isActive()) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Navigate")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

    public void trackShareEvent(String action, Story story) {
        if (isActive()) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Share")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

    public void trackVoteEvent(String action, Story story) {
        if (isActive()) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Vote")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

    public void trackBookmarkEvent(String action, Story story) {
        if (isActive()) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Bookmark")
                    .setAction(action)
                    .setValue(story.getId())
                    .build());
        }
    }

}
