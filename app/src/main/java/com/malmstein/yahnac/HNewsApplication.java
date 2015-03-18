package com.malmstein.yahnac;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.firebase.client.Firebase;
import com.malmstein.yahnac.base.StrictModeManager;
import com.malmstein.yahnac.inject.DefaultDependenciesFactory;
import com.malmstein.yahnac.inject.Inject;
import com.novoda.notils.logger.simple.Log;

public class HNewsApplication extends Application {

    private static final String LOG_TAG = "Yahnac";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Firebase.setAndroidContext(this);
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

    public static Context context() {
        return context;
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
