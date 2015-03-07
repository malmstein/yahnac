package com.malmstein.yahnac;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.yahnac.base.ColorTweaker;
import com.malmstein.yahnac.base.LollipopUiConfiguration;
import com.malmstein.yahnac.base.LollipopUiHelper;
import com.malmstein.yahnac.base.Navigator;
import com.malmstein.yahnac.views.toolbar.AppBarContainer;
import com.malmstein.yahnac.views.toolbar.HNToolbar;
import com.novoda.notils.caster.Views;

public class HNewsActivity extends ActionBarActivity {

    private AppBarContainer appBarContainer;
    private HNToolbar appBar;

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

    public HNToolbar getAppBar() {
        return appBar;
    }

    protected AppBarContainer getAppBarContainer() {
        return appBarContainer;
    }

    private void findAndSetAppBarIfAny() {
        Toolbar toolbar = Views.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        if (toolbar != null) {
            super.setSupportActionBar(toolbar);
        }
        this.appBar = (HNToolbar) toolbar;
        appBarContainer = Views.findById(this, R.id.app_bar_container);
        appBarContainer.setAppBar(appBar);
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

    protected Navigator navigate(){
        if (navigator == null){
            navigator = new Navigator(this);
        }
        return navigator;
    }

}
