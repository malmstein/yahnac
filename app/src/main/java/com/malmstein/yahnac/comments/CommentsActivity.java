package com.malmstein.yahnac.comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.base.DeveloperError;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.SnackBarView;
import com.malmstein.yahnac.views.StoryHeaderView;
import com.novoda.notils.caster.Views;

public class CommentsActivity extends HNewsActivity {

    private ShareActionProvider mShareActionProvider;
    private StoryHeaderView storyHeaderView;
    private SnackBarView snackbarView;

    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        storyHeaderView = Views.findById(this, R.id.story_header_view);

        setupSubActivityWithTitle();
        setupStoryHeader();
        setupSnackbar();

        if (findCommentsFragment() == null) {
            Story story = (Story) getIntent().getExtras().getSerializable(CommentsFragment.ARG_STORY);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.comments_fragment_root, CommentsFragment.from(story), CommentsFragment.TAG).commit();
        }
    }

    private void setupSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareArticleIntent());
        }
        if (getStory().isHackerNewsLocalItem()) {
            MenuItem comments = menu.findItem(R.id.action_article);
            comments.setVisible(false);
        }

        MenuItem bookmarks = menu.findItem(R.id.action_bookmark);
        if (getStory().isBookmark()) {
            checkBookmarkMenuItem(bookmarks);
        } else {
            uncheckBookmarkMenuItem(bookmarks);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_article:
                navigate().toInnerBrowser(getStory());
                return true;
            case R.id.action_bookmark:
                onBookmarkClicked(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void checkBookmarkMenuItem(MenuItem bookmarks) {
        bookmarks.setChecked(true);
        bookmarks.setIcon(R.drawable.ic_bookmark_white);
    }

    private void uncheckBookmarkMenuItem(MenuItem bookmarks) {
        bookmarks.setChecked(false);
        bookmarks.setIcon(R.drawable.ic_bookmark_outline_white);
    }

    private void onBookmarkClicked(MenuItem item) {
        DataPersister persister = Inject.dataPersister();
        if (item.isChecked()) {
            removeBookmark(persister, getStory());
            uncheckBookmarkMenuItem(item);
        } else {
            addBookmark(persister, getStory());
            item.setChecked(true);
            item.setIcon(R.drawable.ic_bookmark_white);
        }
        getStory().toggleBookmark();
    }

    private Intent createShareArticleIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getStory().getCommentsUrl());
        return shareIntent;
    }

    private CommentsFragment findCommentsFragment() {
        return (CommentsFragment) getSupportFragmentManager().findFragmentByTag(CommentsFragment.TAG);
    }

    private void setupStoryHeader() {
        setTitle(getStoryTitle());
        storyHeaderView.updateWith(getStory());
    }

    private Story getStory() {
        if (getIntent().getExtras().containsKey(CommentsFragment.ARG_STORY)) {
            return (Story) getIntent().getExtras().getSerializable(CommentsFragment.ARG_STORY);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    private String getStoryTitle() {
        return getResources().getQuantityString(R.plurals.story_comments,
                getStory().getComments(),
                getStory().getComments());
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

}
