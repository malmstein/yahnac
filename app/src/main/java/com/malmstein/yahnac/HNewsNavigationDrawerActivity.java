package com.malmstein.yahnac;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.malmstein.yahnac.views.drawer.ActionBarDrawerListener;
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
        }

    }

    protected void refreshHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.design_navigation_item_header, null, true);
        navigationView.addHeaderView(header);
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
