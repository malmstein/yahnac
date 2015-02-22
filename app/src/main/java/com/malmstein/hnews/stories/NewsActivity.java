package com.malmstein.hnews.stories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.comments.CommentsFragment;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.presenters.StoriesPagerAdapter;
import com.malmstein.hnews.settings.SettingsActivity;
import com.malmstein.hnews.views.sliding_tabs.SlidingTabLayout;
import com.malmstein.hnews.views.toolbar.AppBarContainer;
import com.novoda.notils.caster.Views;

public class NewsActivity extends HNewsActivity implements StoryListener, AppBarContainer.Listener {

    private static final int OFFSCREEN_PAGE_LIMIT = 1;

    private AppBarContainer appBarContainer;
    private ViewPager headersPager;
    private SlidingTabLayout slidingTabs;
    private StoriesPagerAdapter headersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setupCategories();
        setupTabsAndHeaders();
    }

    private void setupCategories() {
        headersAdapter = new StoriesPagerAdapter(getSupportFragmentManager());
        headersPager = Views.findById(this, R.id.news_pager);
        headersPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        headersPager.setAdapter(headersAdapter);
    }

    private void setupTabsAndHeaders() {
        getAppBarContainer().setListener(this);
        appBarContainer = Views.findById(this, R.id.app_bar_container);
        appBarContainer.setAppBar(getAppBar());

        slidingTabs = Views.findById(this, R.id.sliding_tabs);

        slidingTabs.setOnPageChangeListener(new CategoryOnPageChangeListener());
        slidingTabs.setCustomTabView(R.layout.view_tab_indicator, android.R.id.text1);
        slidingTabs.setSelectedIndicatorColors(getResources().getColor(R.color.feed_tabs_selected_indicator));
        slidingTabs.setViewPager(headersPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShareClicked(Intent shareIntent) {
        startActivity(shareIntent);
    }

    @Override
    public void onCommentsClicked(View v, Story story) {
        showOrNavigateToCommentsOf(story);
    }

    @Override
    public void onContentClicked(Story story) {
        showOrNavigateTo(story);
    }

    private boolean isTwoPaneLayout() {
        return findViewById(R.id.story_fragment_root) != null;
    }

    private void showOrNavigateTo(Story story) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean preferInternalBrowser = preferences.getBoolean(getString(R.string.pref_enable_browser_key), Boolean.valueOf(getString(R.string.pref_enable_browser_default)));

        if (story.getType().equals(Story.TYPE.ask.toString())) {
            navigate().toComments(story);
        } else {
            if (preferInternalBrowser) {
                if (isTwoPaneLayout()) {
                    showInnerBrowserFragment(story);
                } else {
                    navigate().toInnerBrowser(story);
                }
            } else {
                navigate().toExternalBrowser(Uri.parse(story.getUrl()));
            }
        }
    }

    private void showOrNavigateToCommentsOf(Story story) {
        if (isTwoPaneLayout()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.story_fragment_root,
                            CommentsFragment.from(story),
                            CommentsFragment.TAG)
                    .commit();
        } else {
            navigate().toComments(story);
        }
    }

    public void showInnerBrowserFragment(Story story) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.story_fragment_root,
                        ArticleFragment.from(story.getId(), story.getTitle()),
                        CommentsFragment.TAG)
                .commit();
    }

    @Override
    public void onTopInsetChanged(int topInset) {

    }

    private class CategoryOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                return;
            }

            appBarContainer.showAppBar();
        }

    }

}
