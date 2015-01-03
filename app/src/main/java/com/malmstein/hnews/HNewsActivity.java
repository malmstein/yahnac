package com.malmstein.hnews;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.hnews.base.ColorTweaker;
import com.malmstein.hnews.base.LollipopUiConfiguration;
import com.malmstein.hnews.base.LollipopUiHelper;
import com.malmstein.hnews.views.toolbar.HNToolbar;
import com.novoda.notils.caster.Views;

public class HNewsActivity extends ActionBarActivity {

    private HNToolbar appBar;

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

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        findAndSetAppBarIfAny();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        findAndSetAppBarIfAny();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        findAndSetAppBarIfAny();
    }

    protected HNToolbar getAppBar() {
        return appBar;
    }

    private void findAndSetAppBarIfAny() {
        Toolbar toolbar = Views.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        Resources resources = getResources();
        setAppBarColor(getDefaultAppBarColor(resources));
        setStatusBarColorMaybe(getDefaultStatusBarColor(resources));
    }

    protected void setAppBarColor(int color) {
        if (appBar != null) {
            appBar.setBackgroundColor(color);
        }
    }

    protected int getDefaultAppBarColor(Resources resources) {
        return resources.getColor(R.color.orange);
    }

    protected void setStatusBarColorMaybe(int color) {
        lollipopUiHelper.setStatusBarColorMaybe(color);
    }

    protected int getDefaultStatusBarColor(Resources resources) {
        return resources.getColor(R.color.dark_orange);
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
