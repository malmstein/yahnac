package com.malmstein.yahnac.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.about_settings);
        addPreferenceClickListenerForSoftwareLicenses();
        updateSummaryPreferences();
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
}
