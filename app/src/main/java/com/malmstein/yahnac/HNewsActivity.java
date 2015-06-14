package com.malmstein.yahnac;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.yahnac.base.ColorTweaker;
import com.malmstein.yahnac.base.LollipopUiConfiguration;
import com.malmstein.yahnac.base.LollipopUiHelper;
import com.malmstein.yahnac.base.Navigator;

public class HNewsActivity extends AppCompatActivity {

    private ColorTweaker colorTweaker;
    private LollipopUiHelper lollipopUiHelper;
    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colorTweaker = new ColorTweaker();
        lollipopUiHelper = new LollipopUiHelper(this, colorTweaker, getLollipopUiConfiguration());
        lollipopUiHelper.setTaskDescriptionOnLollipopAndLater();
        lollipopUiHelper.setSystemBarsColorOnLollipopAndLater();
        navigator = new Navigator(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setupToolbar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setHighLevelActivity() {
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
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

    public Navigator navigate() {
        if (navigator == null){
            navigator = new Navigator(this);
        }
        return navigator;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);

    }
}
