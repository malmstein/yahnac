package com.malmstein.yahnac.bookmarks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.malmstein.yahnac.HNewsNavigationDrawerActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.injection.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.stories.StoryListener;
import com.malmstein.yahnac.views.SnackBarView;
import com.malmstein.yahnac.views.drawer.ActionBarDrawerListener;
import com.malmstein.yahnac.views.drawer.NavigationDrawerHeader;
import com.novoda.notils.caster.Views;

public class BookmarksActivity extends HNewsNavigationDrawerActivity implements StoryListener, ActionBarDrawerListener.Listener, NavigationDrawerHeader.Listener {

    private static final CharSequence SHARE_DIALOG_DEFAULT_TITLE = null;

    private SnackBarView snackbarView;
    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Inject.usageAnalytics().trackPage(getString(R.string.analytics_page_bookmarks));
    }

    private void setupViews() {
        setupSnackbar();
        setupAppBar();
    }

    private void setupAppBar() {
        setHighLevelActivity();
        setTitle(getString(R.string.title_bookmarks));
    }

    private void setupSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
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
        Inject.usageAnalytics().trackNavigateEvent(getString(R.string.analytics_event_view_comments_bookmarks),
                story);
        navigate().toComments(story);
    }

    @Override
    public void onContentClicked(Story story) {
        Inject.usageAnalytics().trackNavigateEvent(getString(R.string.analytics_event_view_story_bookmarks),
                story);
        DataPersister persister = Inject.dataPersister();
        persister.markStoryAsRead(story);
        navigate().toInnerBrowser(story);
    }

    @Override
    public void onExternalLinkClicked(Story story) {
        if (story.isHackerNewsLocalItem()) {
            Inject.usageAnalytics().trackNavigateEvent(getString(R.string.analytics_event_view_comments_bookmarks),
                    story);
            navigate().toComments(story);
        } else {
            Inject.usageAnalytics().trackNavigateEvent(getString(R.string.analytics_event_view_external_url_bookmarks),
                    story);
            navigate().toExternalBrowser(Uri.parse(story.getUrl()));
        }
    }

    @Override
    public void onBookmarkAdded(Story story) {
        Inject.usageAnalytics().trackBookmarkEvent(getString(R.string.analytics_event_add_bookmark_bookmarks),
                story);
        DataPersister persister = Inject.dataPersister();
        showAddedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onBookmarkRemoved(Story story) {
        Inject.usageAnalytics().trackBookmarkEvent(getString(R.string.analytics_event_remove_bookmark_bookmarks),
                story);
        DataPersister persister = Inject.dataPersister();
        showRemovedBookmarkSnackbar(persister, story);
    }

    @Override
    public void onStoryVoteClicked(Story story) {
        //no op
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
        Inject.usageAnalytics().trackBookmarkEvent(getString(R.string.analytics_event_remove_bookmark_bookmarks),
                story);
        persister.removeBookmark(story);
        showRemovedBookmarkSnackbar(persister, story);
    }

    private void addBookmark(DataPersister persister, Story story) {
        Inject.usageAnalytics().trackBookmarkEvent(getString(R.string.analytics_event_add_bookmark_bookmarks),
                story);
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
        navigate().toLogin(Views.findById(this, R.id.view_drawer_header_login));
    }
}
