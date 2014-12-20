package com.malmstein.hnews;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.malmstein.hnews.base.ColorTweaker;
import com.malmstein.hnews.base.LollipopUiConfiguration;
import com.malmstein.hnews.base.LollipopUiHelper;

public class HNewsActivity extends ActionBarActivity {

    private ColorTweaker colorTweaker;
    private LollipopUiHelper lollipopUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorTweaker = new ColorTweaker();
        lollipopUiHelper = new LollipopUiHelper(this, colorTweaker, getLollipopUiConfiguration());
        lollipopUiHelper.setTaskDescriptionOnLollipopAndLater();
        lollipopUiHelper.setSystemBarsColorOnLollipopAndLater();
    }

    protected LollipopUiConfiguration getLollipopUiConfiguration() {
        return LollipopUiConfiguration.NEWS;
    }

    protected void setupSubActivity() {
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void setupSubActivityWithTitle() {
        setupSubActivity();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

}
