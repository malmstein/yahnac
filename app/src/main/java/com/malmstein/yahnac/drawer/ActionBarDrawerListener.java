package com.malmstein.yahnac.drawer;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.malmstein.yahnac.HNewsNavigationDrawerActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.base.Navigator;

public final class ActionBarDrawerListener extends ActionBarDrawerToggle implements NavDrawerListener {

    private final HNewsNavigationDrawerActivity activity;
    private NavigationTarget pendingNavigation;

    private ActionBarDrawerListener(HNewsNavigationDrawerActivity activity, DrawerLayout drawerLayout,
                                    int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, activity.getAppBar(), openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
    }

    public static ActionBarDrawerListener from(HNewsNavigationDrawerActivity activity, DrawerLayout drawerLayout) {
        return new ActionBarDrawerListener(activity, drawerLayout,
                R.string.navigation_drawer_open_content_description,
                R.string.navigation_drawer_closed_content_description);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        activity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        activity.invalidateOptionsMenu();

        if (pendingNavigation != null) {
            pendingNavigation.navigateUsing(activity.navigate());
            pendingNavigation = null;
        }
    }

    @Override
    public void onSettingsClicked() {
        pendingNavigation = new SettingsNavigationTarget();
        activity.closeDrawer();
    }

    @Override
    public void onNewsClicked() {
        pendingNavigation = new NewsNavigationTarget();
        activity.closeDrawer();
    }

    interface NavigationTarget {
        void navigateUsing(Navigator navigator);
    }

    static class NewsNavigationTarget implements NavigationTarget {

        @Override
        public void navigateUsing(Navigator navigator) {
            navigator.toNews();
        }

    }

    static class SettingsNavigationTarget implements NavigationTarget {

        @Override
        public void navigateUsing(Navigator navigator) {
            navigator.toSettings();
        }

    }

}
