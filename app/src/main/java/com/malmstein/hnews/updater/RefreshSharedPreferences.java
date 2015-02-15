package com.malmstein.hnews.updater;

import android.content.Context;
import android.content.SharedPreferences;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.HNewsApplication;

public class RefreshSharedPreferences {

    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".REFRESH_PREFERENCES";
    private static final String KEY_REFRESH_HISTORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_PREFERENCES_LAST_REFRESH";

    private final SharedPreferences preferences;

    private RefreshSharedPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static RefreshSharedPreferences newInstance() {
        SharedPreferences preferences = HNewsApplication.context().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new RefreshSharedPreferences(preferences);
    }

    public void saveRefreshTick() {
        preferences.edit().putLong(KEY_REFRESH_HISTORY, RefreshTimestamp.now().getMillis()).apply();
    }

    public RefreshTimestamp getLastRefresh() {
        return RefreshTimestamp.from(preferences.getLong(KEY_REFRESH_HISTORY, 0));
    }

}
