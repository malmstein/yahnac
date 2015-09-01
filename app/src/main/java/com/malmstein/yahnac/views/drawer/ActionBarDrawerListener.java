package com.malmstein.yahnac.views.drawer;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.malmstein.yahnac.HNewsNavigationDrawerActivity;
import com.malmstein.yahnac.Navigator;
import com.malmstein.yahnac.R;
import com.novoda.notils.caster.Classes;

public final class ActionBarDrawerListener implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener {

    private final HNewsNavigationDrawerActivity activity;
    private final Listener listener;
    private final DrawerLayout drawerLayout;
    private NavigationTarget pendingNavigation;

    public ActionBarDrawerListener(HNewsNavigationDrawerActivity activity, DrawerLayout drawerLayout) {
        this.activity = activity;
        this.listener = Classes.from(activity);
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        activity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        activity.invalidateOptionsMenu();

        if (pendingNavigation != null) {
            pendingNavigation.navigateUsing(activity.navigate());
            pendingNavigation = null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_news:
                pendingNavigation = new NewsNavigationTarget();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_bookmarks:
                pendingNavigation = new BookmarksNavigationTarget();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_settings:
                pendingNavigation = new SettingsNavigationTarget();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_comments:
                listener.onNotImplementedFeatureSelected();
                drawerLayout.closeDrawers();
                break;

        }
        return false;
    }

    public interface Listener {
        void onNotImplementedFeatureSelected();
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

    static class BookmarksNavigationTarget implements NavigationTarget {

        @Override
        public void navigateUsing(Navigator navigator) {
            navigator.toBookmarks();
        }

    }

}
