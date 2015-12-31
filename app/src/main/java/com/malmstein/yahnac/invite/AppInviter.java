package com.malmstein.yahnac.invite;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.data.updater.AppInviteSharedPreferences;

public class AppInviter {

    private AppInviteSharedPreferences appInviteSharedPreferences;

    public AppInviter() {
        this.appInviteSharedPreferences = AppInviteSharedPreferences.newInstance();
    }

    public boolean shouldShow() {
        return BuildConfig.ENABLE_APP_INVITES && meetsCriteria();
    }

    private boolean meetsCriteria() {
        return appInviteSharedPreferences.isFirstTime() || appInviteSharedPreferences.shouldBeReminded();
    }

}
