package com.malmstein.yahnac.settings;

import android.os.Bundle;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;

/**
 * A {@link android.preference.PreferenceActivity} that presents a set of application settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends HNewsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupSubActivityWithTitle();
    }

}