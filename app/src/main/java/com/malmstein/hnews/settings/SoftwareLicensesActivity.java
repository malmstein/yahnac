package com.malmstein.hnews.settings;

import android.os.Bundle;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;

public class SoftwareLicensesActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_licenses);
        setupSubActivityWithTitle();
    }

}
