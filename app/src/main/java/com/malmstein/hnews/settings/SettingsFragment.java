package com.malmstein.hnews.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.malmstein.hnews.R;
import com.malmstein.hnews.sync.HNewsSyncAdapter;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    boolean mBindingPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sync_time_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        mBindingPreference = true;

        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, getString(R.string.pref_sync_time_default));

        mBindingPreference = false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (!mBindingPreference) {
            if (preference.getKey().equals(getString(R.string.pref_sync_time_key))) {
                int syncInterval = Integer.valueOf(stringValue);
                int flexIntervalTime = syncInterval / 3;
                HNewsSyncAdapter.configurePeriodicSync(getActivity(), syncInterval, flexIntervalTime);
            }
        }

        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(stringValue);
        }
        return true;
    }
}
