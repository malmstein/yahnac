package com.malmstein.hnews.stories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.malmstein.hnews.views.sliding_tabs.SlidingTabLayout;
import com.malmstein.hnews.views.toolbar.AppBarContainer;
import com.novoda.notils.caster.Views;

public class NewsActivity extends HNewsActivity implements StoryListener {

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
        appBarContainer = Views.findById(this, R.id.app_bar_container);
        appBarContainer.setAppBar(getAppBar());
        setTitle(getString(R.string.title_app));

        slidingTabs = Views.findById(this, R.id.sliding_tabs);
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
        if (item.getItemId() == R.id.action_about) {
            navigate().toSettings();
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

    @Override
    public void onExternalLinkClicked(Story story) {
        navigate().toExternalBrowser(Uri.parse(story.getUrl()));
    }

    private boolean isTwoPaneLayout() {
        return findViewById(R.id.story_fragment_root) != null;
    }

    private void showOrNavigateTo(Story story) {
        if (story.isHackerNewsLocalItem()) {
            navigate().toComments(story);
        } else {
            if (isTwoPaneLayout()) {
                showInnerBrowserFragment(story);
            } else {
                navigate().toInnerBrowser(story);
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
                        ArticleFragment.from(story),
                        CommentsFragment.TAG)
                .commit();
    }

}
