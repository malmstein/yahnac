package com.malmstein.yahnac.data.updater;

import android.content.Context;
import android.content.SharedPreferences;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.HNewsApplication;
import com.malmstein.yahnac.model.Story;

public class RefreshSharedPreferences {

    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".REFRESH_PREFERENCES";

    private static final String KEY_REFRESH_TIME_TOP_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_TOP_STORY";
    private static final String KEY_REFRESH_TIME_NEW_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_NEW_STORY";
    private static final String KEY_REFRESH_TIME_BEST_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_BEST_STORY";
    private static final String KEY_REFRESH_TIME_SHOW_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_SHOW_STORY";
    private static final String KEY_REFRESH_TIME_ASK_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_ASK_STORY";
    private static final String KEY_REFRESH_TIME_JOB_STORY = BuildConfig.APPLICATION_ID + ".KEY_REFRESH_TIME_JOB_STORY";

    private final SharedPreferences preferences;

    private RefreshSharedPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static RefreshSharedPreferences newInstance() {
        SharedPreferences preferences = HNewsApplication.context().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new RefreshSharedPreferences(preferences);
    }

    public void saveRefreshTick(Story.FILTER FILTER) {
        switch (FILTER) {
            case top_story:
                preferences.edit().putLong(KEY_REFRESH_TIME_TOP_STORY, RefreshTimestamp.now().getMillis()).apply();
                break;
            case new_story:
                preferences.edit().putLong(KEY_REFRESH_TIME_NEW_STORY, RefreshTimestamp.now().getMillis()).apply();
                break;
            case best_story:
                preferences.edit().putLong(KEY_REFRESH_TIME_BEST_STORY, RefreshTimestamp.now().getMillis()).apply();
                break;
            case show:
                preferences.edit().putLong(KEY_REFRESH_TIME_SHOW_STORY, RefreshTimestamp.now().getMillis()).apply();
                break;
            case ask:
                preferences.edit().putLong(KEY_REFRESH_TIME_ASK_STORY, RefreshTimestamp.now().getMillis()).apply();
                break;
            case jobs:
                preferences.edit().putLong(KEY_REFRESH_TIME_JOB_STORY, RefreshTimestamp.now().getMillis()).apply();
                break;
        }

    }

    public RefreshTimestamp getLastRefresh(Story.FILTER FILTER) {
        switch (FILTER) {
            case top_story:
                return RefreshTimestamp.from(preferences.getLong(KEY_REFRESH_TIME_TOP_STORY, 0));
            case new_story:
                return RefreshTimestamp.from(preferences.getLong(KEY_REFRESH_TIME_NEW_STORY, 0));
            case best_story:
                return RefreshTimestamp.from(preferences.getLong(KEY_REFRESH_TIME_BEST_STORY, 0));
            case show:
                return RefreshTimestamp.from(preferences.getLong(KEY_REFRESH_TIME_SHOW_STORY, 0));
            case ask:
                return RefreshTimestamp.from(preferences.getLong(KEY_REFRESH_TIME_ASK_STORY, 0));
            case jobs:
                return RefreshTimestamp.from(preferences.getLong(KEY_REFRESH_TIME_JOB_STORY, 0));
            default:
                return RefreshTimestamp.now();
        }
    }

}
