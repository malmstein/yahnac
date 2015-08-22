package com.malmstein.yahnac.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.malmstein.yahnac.R;

public class UsageAnalytics {

    private Tracker analyticsTracker;

    public void initTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analyticsTracker = analytics.newTracker(R.xml.global_tracker);
    }

    public void trackPage(String page) {
        analyticsTracker.setScreenName(page);
        analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackerEvent(String category, String action) {
        analyticsTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

}
