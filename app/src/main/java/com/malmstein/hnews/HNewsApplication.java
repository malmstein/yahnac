package com.malmstein.hnews;

import android.app.Application;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.malmstein.hnews.base.StrictModeManager;
import com.malmstein.hnews.inject.DefaultDependenciesFactory;
import com.malmstein.hnews.inject.Inject;
import com.novoda.notils.logger.simple.Log;

public class HNewsApplication extends Application {

    private static final String LOG_TAG = "HNews";

    @Override
    public void onCreate() {
        super.onCreate();
        Inject.using(new DefaultDependenciesFactory(this));
        startup();
    }

    private void startup() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Log.SHOW_LOGS = BuildConfig.DEBUG;
                Log.TAG = LOG_TAG;
                Log.STACK_DEPTH = 6;
                StrictModeManager.initializeStrictMode(newVmPolicyBuilder(), newThreadPolicyBuilder());
                Inject.crashAnalytics().startTracking(getApplicationContext());
                return null;
            }

        }.execute();
    }

    private static StrictMode.VmPolicy.Builder newVmPolicyBuilder() {
        return new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedRegistrationObjects()
                .penaltyLog();
    }

    private static StrictMode.ThreadPolicy.Builder newThreadPolicyBuilder() {
        return new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls()
                .penaltyLog();
    }
}
