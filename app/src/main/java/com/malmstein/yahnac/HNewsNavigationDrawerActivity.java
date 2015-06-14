package com.malmstein.yahnac;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.malmstein.yahnac.drawer.ActionBarDrawerListener;
import com.novoda.notils.caster.Views;

public abstract class HNewsNavigationDrawerActivity extends HNewsActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerListener drawerListener;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        drawer = Views.findById(this, R.id.navigation_drawer);
        drawerListener = new ActionBarDrawerListener(this, drawer);
        drawer.setDrawerListener(drawerListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(drawerListener);
            setupDrawerContent(navigationView);
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    protected DrawerLayout getDrawer() {
        return drawer;
    }

    public void closeDrawer() {
        drawer.closeDrawers();
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
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
