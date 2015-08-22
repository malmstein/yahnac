package com.malmstein.yahnac.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.R;

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

    public void trackEvent(String category, String action) {
        if (isActive()) {
            analyticsTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .build());
        }
    }

}
