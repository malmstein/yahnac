package com.malmstein.yahnac.settings;

import android.os.Bundle;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;

public class SoftwareLicensesActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_licenses);
        setupSubActivityWithTitle();
    }

}
