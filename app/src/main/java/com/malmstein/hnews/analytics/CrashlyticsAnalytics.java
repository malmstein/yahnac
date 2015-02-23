package com.malmstein.hnews.analytics;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.malmstein.hnews.BuildConfig;

public class CrashlyticsAnalytics implements CrashAnalytics {

    @Override
    public boolean isActive() {
        return BuildConfig.ENABLE_CRASH_ANALYTICS;
    }

    @Override
    public void startTracking(Context context) {
        Crashlytics.setString("Flavor", BuildConfig.FLAVOR);
        Crashlytics.setString("Version", BuildConfig.VERSION_NAME);
        Crashlytics.start(context);
    }

    @Override
    public void logSomethingWentWrong(String errorMessage) {
        Crashlytics.log(errorMessage);
    }

    @Override
    public void logSomethingWentWrong(String errorMessage, Throwable throwable) {
        Crashlytics.log(errorMessage);
        Crashlytics.logException(throwable);
    }

}
