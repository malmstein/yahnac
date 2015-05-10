package com.malmstein.yahnac;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.newsuk.thesun.base.inject.Inject;
import com.newsuk.thesun.base.navigation.drawer.NavDrawerAdapter;
import com.novoda.notils.caster.Views;

public abstract class HNewsNavigationDrawerActivity extends HNewsActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerListener drawerListener;

    protected abstract NavDrawerAdapter createNavDrawerAdapter(LayoutInflater layoutInflater, ActionBarDrawerListener drawerListener);

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        drawer = Views.findById(this, R.id.navigation_drawer);
        drawerListener = ActionBarDrawerListener.from(this, drawer, Inject.usageAnalytics());
        drawer.setDrawerListener(drawerListener);

        NavDrawerAdapter drawerAdapter = createNavDrawerAdapter(LayoutInflater.from(this), drawerListener);
        editionWatcher = EditionWatcher.newInstance(Inject.feedProvider(), Inject.feedRepository(), createListenerFor(drawerAdapter));
        ListView drawerList = Views.findById(drawer, R.id.drawer_list);
        drawerList.setAdapter(drawerAdapter);

        setFullscreenLayoutOnLollipopAndLater();
    }

    @Override
    protected int getDefaultStatusBarColor(Resources resources) {
        return resources.getColor(R.color.translucent_system_ui_background);
    }

    private void setFullscreenLayoutOnLollipopAndLater() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    protected DrawerLayout getDrawer() {
        return drawer;
    }

    public void closeDrawer() {
        drawer.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarToggleShouldReplaceUp() && drawerListener != null) {
            drawerListener.syncState();
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldBackPressedBeInterceptedByNavDrawer()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean shouldBackPressedBeInterceptedByNavDrawer() {
        return drawer.isDrawerOpen(Gravity.LEFT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onHomeButtonClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onHomeButtonClick() {
        if (actionBarToggleShouldReplaceUp()) {
            toggleNavigationDrawer();
        } else {
            onActionBarUpClick();
        }
    }

    protected void onActionBarUpClick() {
        NavUtils.navigateUpFromSameTask(this);
    }

    /**
     * Determines whether Up indicator should be replaced by a drawer toggle.
     *
     * @return true if the Up indicator should be replaced by a drawer toggle (burger)
     */
    protected abstract boolean actionBarToggleShouldReplaceUp();

    private void toggleNavigationDrawer() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            drawer.openDrawer(Gravity.START);
        }
    }

}
