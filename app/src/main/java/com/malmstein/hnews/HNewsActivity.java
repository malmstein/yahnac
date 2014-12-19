package com.malmstein.hnews;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HNewsActivity extends ActionBarActivity {

    private Toolbar toolbar;

    protected void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setAppBarColor(getDefaultAppBarColor(getResources()));
    }

    private int getDefaultAppBarColor(Resources resources) {
        return resources.getColor(R.color.orange);
    }

    private void setAppBarColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }

    protected void setupUpIndicatorOn() {
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
