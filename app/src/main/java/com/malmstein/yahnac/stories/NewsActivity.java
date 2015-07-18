package com.malmstein.yahnac.stories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.malmstein.yahnac.HNewsNavigationDrawerActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.drawer.ActionBarDrawerListener;
import com.malmstein.yahnac.drawer.NavigationDrawerHeader;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.presenters.StoriesPagerAdapter;
import com.malmstein.yahnac.views.SnackBarView;
import com.novoda.notils.caster.Views;

public class NewsActivity extends HNewsNavigationDrawerActivity implements StoryListener, ActionBarDrawerListener.Listener, NavigationDrawerHeader.Listener {

    private static final CharSequence SHARE_DIALOG_DEFAULT_TITLE = null;
    private ViewPager headersPager;

    private SnackBarView snackbarView;
    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;

    private StoriesPagerAdapter storiesPagerAdapter;

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

    private void setupHeaders() {
        headersPager = (ViewPager) findViewById(R.id.viewpager);
        storiesPagerAdapter = new StoriesPagerAdapter(getSupportFragmentManager());
        headersPager.setAdapter(storiesPagerAdapter);
    }

    private void setupTabs() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(headersPager);
        tabLayout.setOnTabSelectedListener(new StoryTabSelectedListener());
    }

    private void setupAppBar() {
        setHighLevelActivity();
        setTitle(getString(R.string.title_app));
    }

    private void setupSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHeader();
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
    public void onCommentsClicked(Story story) {
        navigate().toComments(story);
    }

    @Override
    public void onContentClicked(Story story) {
        DataPersister persister = Inject.dataPersister();
        persister.markStoryAsRead(story);
        navigate().toInnerBrowser(story);
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
    public void onBookmarkAdded(Story story) {
        DataPersister persister = Inject.dataPersister();
        showAddedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onBookmarkRemoved(Story story) {
        DataPersister persister = Inject.dataPersister();
        showRemovedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onStoryVoteClicked(Story story) {

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

    private void removeBookmark(DataPersister persister, Story story) {
        persister.removeBookmark(story);
        showRemovedBookmarkSnackbar(persister, story);
    }

    private void addBookmark(DataPersister persister, Story story) {
        persister.addBookmark(story);
        showAddedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onNotImplementedFeatureSelected() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_not_implemented))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .animating();
    }

    @Override
    public void onLoginClicked() {
        navigate().toLogin(Views.findById(this, R.id.view_drawer_header));
    }

    public class StoryTabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            headersPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            String tag = storiesPagerAdapter.getTag(tab.getPosition());
            StoryFragment fragment = (StoryFragment) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.scrollToTop();

        }
    }

}
