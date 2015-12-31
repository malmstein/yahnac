package com.malmstein.yahnac.data.updater;

import android.content.Context;
import android.content.SharedPreferences;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.HNewsApplication;

public class AppInviteSharedPreferences {

    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".INVITE_PREFERENCES";
    private static final String KEY_PROMPTED = BuildConfig.APPLICATION_ID + ".KEY_PROMPTED";
    private static final String KEY_TIMES_REMINDED = BuildConfig.APPLICATION_ID + ".KEY_TIMES_REMINDED";
    private static final String KEY_REMINDED = BuildConfig.APPLICATION_ID + ".KEY_REMINDED";
    private static int timesToRemind = 5;
    private final SharedPreferences preferences;

    private AppInviteSharedPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static AppInviteSharedPreferences newInstance() {
        SharedPreferences preferences = HNewsApplication.context().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new AppInviteSharedPreferences(preferences);
    }

    private boolean hasBeenPrompted() {
        return preferences.getBoolean(KEY_PROMPTED, false);
    }

    public boolean hasBeenReminded() {
        return preferences.getBoolean(KEY_REMINDED, false);
    }

    public void setPrompted() {
        preferences.edit().putBoolean(KEY_PROMPTED, true).apply();
    }

    public void setReminded() {
        preferences.edit().putBoolean(KEY_REMINDED, true).apply();
    }

    private int timesReminded() {
        return preferences.getInt(KEY_TIMES_REMINDED, 0);
    }

    private void addReminded() {
        int reminded = timesReminded() + 1;
        preferences.edit().putInt(KEY_TIMES_REMINDED, reminded).apply();
    }

    public boolean isFirstTime() {
        if (!hasBeenPrompted()) {
            setPrompted();
            return true;
        } else {
            return false;
        }
    }

    public boolean shouldBeReminded() {
        if (!hasBeenReminded()) {
            if (timesReminded() >= timesToRemind) {
                setReminded();
                return true;
            } else {
                addReminded();
                return false;
            }
        } else {
            return false;
        }

    }
}
