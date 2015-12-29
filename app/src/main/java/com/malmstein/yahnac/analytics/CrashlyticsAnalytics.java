package com.malmstein.yahnac.analytics;

import com.crashlytics.android.Crashlytics;

public class CrashlyticsAnalytics implements CrashAnalytics {

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
