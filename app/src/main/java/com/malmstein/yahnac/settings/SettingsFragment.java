package com.malmstein.yahnac.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.updater.LoginSharedPreferences;

public class SettingsFragment extends PreferenceFragment {

    private Listener listener;
    private LoginSharedPreferences loginSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.about_settings);
        addPreferencesFromResource(R.xml.account_settings);
        addPreferenceClickListenerForSoftwareLicenses();
        updateSummaryPreferences();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (Listener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = new DummyListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureAccountPreferences();
    }

    private void configureAccountPreferences() {
        PreferenceCategory accountCategory = (PreferenceCategory) findPreference(getString(R.string.settings_category_key_account));
        Preference logoutNotification = accountCategory.findPreference(getString(R.string.settings_key_logout));
        logoutNotification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                listener.onShowLogoutDialog();
                return true;
            }
        });

        loginSharedPreferences = LoginSharedPreferences.newInstance();
        logoutNotification.setEnabled(loginSharedPreferences.isLoggedIn());

    }

    private void addPreferenceClickListenerForSoftwareLicenses() {
        Preference licensesPreference = findPreference(getString(R.string.settings_key_software_licences));
        licensesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent softwareLicenses = new Intent(getActivity(), SoftwareLicensesActivity.class);
                startActivity(softwareLicenses);
                return true;
            }

        });
    }

    private void updateSummaryPreferences() {
        updateSummary(R.string.settings_key_build_version, String.format(getString(R.string.settings_value_version_number), BuildConfig.VERSION_NAME));
        updateSummary(R.string.settings_key_build_timestamp, BuildConfig.BUILD_TIME_FORMATTED);
    }

    private void updateSummary(int settingsKeyId, String summary) {
        Preference preference = findPreference(getString(settingsKeyId));
        preference.setSummary(summary);
    }

    public interface Listener {

        void onShowLogoutDialog();

    }

    private static class DummyListener implements Listener {

        @Override
        public void onShowLogoutDialog() {
            // no-op
        }

    }
}
