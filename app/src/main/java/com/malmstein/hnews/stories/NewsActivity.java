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

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.comments.CommentFragment;
import com.malmstein.hnews.comments.CommentsActivity;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.presenters.NewsAdapter;
import com.malmstein.hnews.settings.SettingsActivity;
import com.malmstein.hnews.sync.HNewsSyncAdapter;
import com.malmstein.hnews.views.sliding_tabs.SlidingTabLayout;
import com.novoda.notils.caster.Views;

public class NewsActivity extends HNewsActivity implements TopStoriesFragment.Listener {

    private static final int OFFSCREEN_PAGE_LIMIT = 1;

    private ViewPager headersPager;
    private SlidingTabLayout slidingTabs;
    private NewsAdapter headersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        HNewsSyncAdapter.initializeSyncAdapter(this);

        setupCategories();
        setupTabsAndHeaders();
    }

    private void setupCategories() {
        headersAdapter = new NewsAdapter(getSupportFragmentManager());
        headersPager = Views.findById(this, R.id.news_pager);
        headersPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        headersPager.setAdapter(headersAdapter);
    }

    private void setupTabsAndHeaders() {
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
    public void onCommentsClicked(Story story) {
        navigateToComments(story);
    }

    @Override
    public void onContentClicked(Story story) {
        navigateToArticle(story);
    }

    private boolean isTwoPaneLayout() {
        return findViewById(R.id.story_fragment_root) != null;
    }

    private void navigateToArticle(Story story) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean preferInternalBrowser = preferences.getBoolean(getString(R.string.pref_enable_browser_key), Boolean.valueOf(getString(R.string.pref_enable_browser_default)));
        if (preferInternalBrowser) {
            if (isTwoPaneLayout()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.story_fragment_root,
                                ArticleFragment.from(story.getInternalId(), story.getTitle()),
                                ArticleFragment.TAG)
                        .commit();
            } else {
                Intent articleIntent = new Intent(this, ArticleActivity.class);
                articleIntent.putExtra(ArticleFragment.ARG_STORY_ID, story.getInternalId());
                articleIntent.putExtra(ArticleFragment.ARG_STORY_TITLE, story.getTitle());
                startActivity(articleIntent);
            }
        } else {
            navigateToExternalBrowser(Uri.parse(story.getUrl()));
        }
    }

    private void navigateToExternalBrowser(Uri articleUri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(articleUri);
        startActivity(browserIntent);
    }

    private void navigateToComments(Story story) {
        if (isTwoPaneLayout()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.story_fragment_root,
                            CommentFragment.from(story.getId(), story.getTitle()),
                            CommentFragment.TAG)
                    .commit();
        } else {
            Intent commentIntent = new Intent(this, CommentsActivity.class);
            commentIntent.putExtra(CommentFragment.ARG_STORY_ID, story.getId());
            commentIntent.putExtra(CommentFragment.ARG_STORY_TITLE, story.getTitle());
            startActivity(commentIntent);
        }
    }


}
