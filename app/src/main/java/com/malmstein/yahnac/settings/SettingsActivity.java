package com.malmstein.yahnac.settings;

import android.app.Fragment;
import android.os.Bundle;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.updater.LoginSharedPreferences;

/**
 * A {@link android.preference.PreferenceActivity} that presents a set of application settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends HNewsActivity implements SettingsFragment.Listener, LogoutConfirmDialogFragment.Listener {

    private static final String LOGOUT_FRAGMENT_TAG = BuildConfig.APPLICATION_ID + ".LOGOUT_FRAGMENT_TAG";

    private LoginSharedPreferences loginSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupSubActivityWithTitle();

        loginSharedPreferences = LoginSharedPreferences.newInstance();
    }

    @Override
    public void onShowLogoutDialog() {
        showLogoutConfirmationDialog();
    }

    public void showLogoutConfirmationDialog() {
        Fragment fragment = getFragmentManager().findFragmentByTag(LOGOUT_FRAGMENT_TAG);
        if (fragment == null) {
            LogoutConfirmDialogFragment.newInstance().show(getFragmentManager(), LOGOUT_FRAGMENT_TAG);
        }
    }

    @Override
    public void onLogoutConfirmed() {
        loginSharedPreferences.logout();
        finish();
    }

}
