package com.malmstein.yahnac.analytics;

import com.google.firebase.crash.FirebaseCrash;

public class FirebaseAnalytics implements CrashAnalytics {

    @Override
    public void logSomethingWentWrong(String errorMessage) {
        FirebaseCrash.log(errorMessage);
    }

    @Override
    public void logSomethingWentWrong(String errorMessage, Throwable throwable) {
        FirebaseCrash.report(throwable);
    }
}
