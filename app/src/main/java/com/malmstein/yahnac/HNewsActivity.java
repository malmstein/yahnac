package com.malmstein.yahnac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.malmstein.yahnac.data.connectivity.NetworkChecker;
import com.malmstein.yahnac.views.ColorTweaker;
import com.malmstein.yahnac.views.LollipopUiConfiguration;
import com.malmstein.yahnac.views.LollipopUiHelper;

public class HNewsActivity extends AppCompatActivity {

    public static final CharSequence SHARE_DIALOG_DEFAULT_TITLE = null;

    private ColorTweaker colorTweaker;
    private LollipopUiHelper lollipopUiHelper;
    private Navigator navigator;
    private NetworkChecker networkChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNetworkChecker();

        colorTweaker = new ColorTweaker();
        lollipopUiHelper = new LollipopUiHelper(this, colorTweaker, getLollipopUiConfiguration());
        lollipopUiHelper.setTaskDescriptionOnLollipopAndLater();
        lollipopUiHelper.setSystemBarsColorOnLollipopAndLater();
        navigator = new Navigator(this);
    }

    private void initNetworkChecker() {
        networkChecker = new NetworkChecker(this);
    }

    protected LollipopUiConfiguration getLollipopUiConfiguration() {
        return LollipopUiConfiguration.NEWS;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setHighLevelActivity() {
        setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setupSubActivity() {
        setupToolbar();
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
        if (navigator == null) {
            navigator = new Navigator(this);
        }
        return navigator;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    public boolean isOnline() {
        return networkChecker.isConnected();
    }
}
