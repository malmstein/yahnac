package com.malmstein.yahnac.stories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.malmstein.yahnac.HNewsNavigationDrawerActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.drawer.ActionBarDrawerListener;
import com.malmstein.yahnac.drawer.NavDrawerAdapter;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.login.LoginDialog;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.presenters.StoriesPagerAdapter;
import com.malmstein.yahnac.updater.LoginSharedPreferences;
import com.malmstein.yahnac.views.FloatingActionButton;
import com.malmstein.yahnac.views.SnackBarView;
import com.malmstein.yahnac.views.sliding_tabs.SlidingTabLayout;
import com.novoda.notils.caster.Views;

public class NewsActivity extends HNewsNavigationDrawerActivity implements StoryListener, LoginDialog.Listener {

    private static final int OFFSCREEN_PAGE_LIMIT = 1;
    private static final CharSequence SHARE_DIALOG_DEFAULT_TITLE = null;
    private ViewPager headersPager;
    private SlidingTabLayout slidingTabs;
    private StoriesPagerAdapter headersAdapter;

    private SnackBarView snackbarView;
    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;

    private LoginSharedPreferences loginSharedPreferences;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setupViews();
    }

    private void setupViews() {
        setupHeaders();
        setupTabs();
        setupSnackbar();
        setupAppBar();
    }

    private void setupSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    private void setupHeaders() {
        headersAdapter = new StoriesPagerAdapter(getSupportFragmentManager());
        headersPager = Views.findById(this, R.id.news_pager);
        headersPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        headersPager.setAdapter(headersAdapter);
    }

    private void setupTabs() {
        slidingTabs = Views.findById(this, R.id.sliding_tabs);
        slidingTabs.setCustomTabView(R.layout.view_tab_indicator, android.R.id.text1);
        slidingTabs.setViewPager(headersPager);
        slidingTabs.setSelectedIndicatorColors(getResources().getColor(R.color.feed_tabs_selected_indicator));
        slidingTabs.setOnPageChangeListener(new StoryOnPageChangeListener());
    }

    private void setupAppBar() {
        setTitle(getString(R.string.title_app));
    }

    private void setupFab() {
        fab = Views.findById(this, R.id.fab_login);
        if (loginSharedPreferences == null) {
            loginSharedPreferences = LoginSharedPreferences.newInstance();
        }
        if (loginSharedPreferences.isLoggedIn()) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fab.hideAnimated();
                    DialogFragment loginDialog = new LoginDialog();
                    loginDialog.show(getSupportFragmentManager(), LoginDialog.TAG);
                }
            });
        }
    }

    @Override
    protected NavDrawerAdapter createNavDrawerAdapter(LayoutInflater layoutInflater, ActionBarDrawerListener drawerListener) {
        return NavDrawerAdapter.newInstance(layoutInflater, drawerListener);
    }

    @Override
    protected boolean actionBarToggleShouldReplaceUp() {
        return true;
    }

    @Override
    public void onShareClicked(Intent shareIntent) {
        Intent chooserIntent = Intent.createChooser(shareIntent, SHARE_DIALOG_DEFAULT_TITLE);
        startActivity(chooserIntent);
    }

    @Override
    public void onCommentsClicked(View v, Story story) {
        navigate().toComments(v, story);
    }

    @Override
    public void onContentClicked(Story story) {
        DataPersister persister = Inject.dataPersister();
        persister.markStoryAsRead(story);
        if (story.isHackerNewsLocalItem()) {
            navigate().toComments(story);
        } else {
            navigate().toInnerBrowser(story);
        }
    }

    @Override
    public void onExternalLinkClicked(Story story) {
        if (story.isHackerNewsLocalItem()) {
            navigate().toComments(story);
        } else {
            navigate().toExternalBrowser(Uri.parse(story.getUrl()));
        }
    }

    @Override
    public void onBookmarkClicked(Story story) {
        DataPersister persister = Inject.dataPersister();
        if (story.isBookmark()) {
            removeBookmark(persister, story);
        } else {
            addBookmark(persister, story);
        }
    }

    @Override
    public void onQuickReturnVisibilityChangeHint(boolean visible) {
        if (visible) {
            getAppBarContainer().showAppBar();
            fab.showAnimated();
        } else {
            getAppBarContainer().hideAppBar();
            fab.hideAnimated();
        }
    }

    private void removeBookmark(DataPersister persister, Story story) {
        persister.removeBookmark(story);
        showRemovedBookmarkSnackbar(persister, story);
    }

    private void addBookmark(DataPersister persister, Story story) {
        persister.addBookmark(story);
        showAddedBookmarkSnackbar(persister, story);
    }

    private void showAddedBookmarkSnackbar(final DataPersister persister, final Story story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_added_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarView.hideCrouton();
                        removeBookmark(persister, story);
                    }
                })
                .animating();
    }

    private void showRemovedBookmarkSnackbar(final DataPersister persister, final Story story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_removed_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarView.hideCrouton();
                        addBookmark(persister, story);
                    }
                })
                .animating();
    }

    @Override
    public void onLoginSucceed() {
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onLoginCancelled() {
        fab.showAnimated();
    }

    private class StoryOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset == 0) {
                return;
            }
            scrollFeedsAccordingToAppBarVisibility();
        }

        private void scrollFeedsAccordingToAppBarVisibility() {
            Fragment currentFragment = headersAdapter.getPrimaryItem();
            int px = getAppBarContainer().isAppBarShowing() ? 0 : getAppBarContainer().getHideableHeightPx();
            for (int i = 0; i < headersAdapter.getCount(); i++) {
                StoryFragment fragment = getStoryFragmentAt(i);
                if (fragment == null || fragment == currentFragment) {
                    continue;
                }
                if (fragment.shouldBeScrolledToTop()) {
                    fragment.scrollToTopWithOffset(px);
                }
            }
        }

        private StoryFragment getStoryFragmentAt(int position) {
            String tag = headersAdapter.getTag(position);
            return (StoryFragment) getSupportFragmentManager().findFragmentByTag(tag);
        }

    }
}
