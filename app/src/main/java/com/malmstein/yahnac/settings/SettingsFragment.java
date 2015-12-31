package com.malmstein.yahnac.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
        addPreferencesFromResource(R.xml.community_settings);
        configureAccountPreferences();
        addPreferenceClickListenerForSoftwareLicenses();
        addPreferenceClickListenerForCommunity();
        addPreferenceClickListenerForAppInvite();
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

    private void configureAccountPreferences() {
        loginSharedPreferences = LoginSharedPreferences.newInstance();
        if (loginSharedPreferences.isLoggedIn()) {
            addPreferencesFromResource(R.xml.account_settings);
            PreferenceCategory accountCategory = (PreferenceCategory) findPreference(getString(R.string.settings_category_key_account));
            Preference logoutNotification = accountCategory.findPreference(getString(R.string.settings_key_logout));
            logoutNotification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    listener.onShowLogoutDialog();
                    return true;
                }
            });
        }
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

    private void addPreferenceClickListenerForCommunity() {
        PreferenceCategory communityCategory = (PreferenceCategory) findPreference(getString(R.string.settings_category_key_community));
        Preference communityPreference = communityCategory.findPreference(getString(R.string.settings_key_community));
        communityPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/communities/108233780766400792163"));
                startActivity(browserIntent);
                return true;
            }

        });
    }

    private void addPreferenceClickListenerForAppInvite() {
        PreferenceCategory communityCategory = (PreferenceCategory) findPreference(getString(R.string.settings_category_key_community));
        Preference invitePreference = communityCategory.findPreference(getString(R.string.settings_key_invite));
        invitePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                listener.onAppInviteRequested();
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

        void onAppInviteRequested();

    }

    private static class DummyListener implements Listener {

        @Override
        public void onShowLogoutDialog() {
            // no-op
        }

        @Override
        public void onAppInviteRequested() {
            // no-op
        }

    }
}
