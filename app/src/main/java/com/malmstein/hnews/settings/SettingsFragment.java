package com.malmstein.hnews.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.malmstein.hnews.R;
import com.malmstein.hnews.sync.HNewsSyncAdapter;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    boolean mBindingPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        bindPreferences();
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_general, false);
    }

    private void bindPreferences() {
        mBindingPreference = true;
        bindSyncTimePreference(findPreference(getString(R.string.pref_sync_time_key)));
        bindSyncPreference(findPreference(getString(R.string.pref_sync_key)));
        bindBrowserPreference(findPreference(getString(R.string.pref_enable_browser_key)));
        mBindingPreference = false;
    }

    private void bindSyncTimePreference(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, getString(R.string.pref_sync_time_default));
    }

    private void bindSyncPreference(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, getString(R.string.pref_sync_default));
    }

    private void bindBrowserPreference(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, getString(R.string.pref_enable_browser_default));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (!mBindingPreference) {
            if (preference.getKey().equals(getString(R.string.pref_sync_time_key))) {
                String stringValue = newValue.toString();
                int syncInterval = Integer.valueOf(stringValue);
                int flexIntervalTime = syncInterval / 3;
                HNewsSyncAdapter.configurePeriodicSync(getActivity(), syncInterval, flexIntervalTime);
            }

            if (preference.getKey().equals(getString(R.string.pref_sync_key))) {
                boolean booleanValue = (boolean) newValue;
                if (booleanValue) {
                    int syncInterval = Integer.valueOf(getString(R.string.pref_sync_time_default));
                    int flexIntervalTime = syncInterval / 3;
                    HNewsSyncAdapter.configurePeriodicSync(getActivity(), syncInterval, flexIntervalTime);
                } else {
                    HNewsSyncAdapter.removePeriodicSyncs(getActivity());
                }
            }
        }

        if (preference instanceof ListPreference) {
            String stringValue = newValue.toString();
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        return true;
    }
}
