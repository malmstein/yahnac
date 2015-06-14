package com.malmstein.yahnac.stories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.malmstein.yahnac.HNewsNavigationDrawerActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.login.LoginDialog;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.presenters.StoriesPagerAdapter;
import com.malmstein.yahnac.updater.LoginSharedPreferences;
import com.malmstein.yahnac.views.SnackBarView;
import com.novoda.notils.caster.Views;

public class NewsActivity extends HNewsNavigationDrawerActivity implements StoryListener, LoginDialog.Listener {

    private static final CharSequence SHARE_DIALOG_DEFAULT_TITLE = null;
    private ViewPager headersPager;

    private SnackBarView snackbarView;
    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;

    private LoginSharedPreferences loginSharedPreferences;

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
        if (headersPager != null) {
            headersPager.setAdapter(new StoriesPagerAdapter(getSupportFragmentManager()));
        }
    }

    private void setupTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(headersPager);
    }

    private void setupAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_app));
    }

    private void setupSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    private void setupFab() {
        DialogFragment loginDialog = new LoginDialog();
        loginDialog.show(getSupportFragmentManager(), LoginDialog.TAG);
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
//        fab.setVisibility(View.GONE);
    }

    @Override
    public void onLoginCancelled() {
//        fab.showAnimated();
    }

}
