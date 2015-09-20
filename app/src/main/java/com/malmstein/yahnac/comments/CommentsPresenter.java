package com.malmstein.yahnac.comments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.data.updater.LoginSharedPreferences;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.SnackBarView;
import com.malmstein.yahnac.views.StoryHeaderView;
import com.novoda.notils.caster.Views;
import com.novoda.notils.exception.DeveloperError;

public class CommentsPresenter implements ReplyView.Listener, CommentsAdapter.Listener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private static final int COMMENTS_LOADER = 1006;

    private final HNewsActivity activity;
    private final SwipeRefreshLayout.OnRefreshListener refreshListener;
    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;
    private LoginSharedPreferences loginSharedPreferences;
    private StoryHeaderView storyHeaderView;
    private SnackBarView snackbarView;
    private ReplyView replyView;
    private FloatingActionButton replyFab;
    private CommentsView commentsView;
    private Animator mCircularReveal;

    public CommentsPresenter(HNewsActivity activity, SwipeRefreshLayout.OnRefreshListener refreshListener) {
        this.activity = activity;
        this.refreshListener = refreshListener;
    }

    private Story getStory() {
        if (activity.getIntent().getExtras().containsKey(CommentsActivity.ARG_STORY)) {
            return (Story) activity.getIntent().getExtras().getSerializable(CommentsActivity.ARG_STORY);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    public void onCreate() {
        storyHeaderView = Views.findById(activity, R.id.story_header_view);
        replyFab = Views.findById(activity, R.id.story_reply_action);
        replyView = Views.findById(activity, R.id.reply_view);
        snackbarView = Views.findById(activity, R.id.snackbar);
        commentsView = Views.findById(activity, R.id.comments_view);

        ViewCompat.setTransitionName(storyHeaderView, VIEW_NAME_HEADER_TITLE);

        activity.setupSubActivity();

        setupHeaderView();
        setupSnackbar();
        setupCommentsView();
        setupReplyListener();
        loadComments();
    }

    public void onPostCreate(boolean online) {
        onRefresh(online);
    }

    public void onCreateOptionsMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_comments, menu);

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
    }

    public void onBookmarkUnselected(MenuItem item) {
        uncheckBookmarkMenuItem(item);
    }

    public void onBookmarkSelected(MenuItem item) {
        item.setChecked(true);
        item.setIcon(R.drawable.ic_bookmark_white);
    }

    private void setupHeaderView() {
        storyHeaderView.updateWith(getStory());
    }

    private void setupSnackbar() {
        croutonBackgroundAlpha = activity.getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = activity.getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    private void setupCommentsView() {
        commentsView.setupWith(this, refreshListener, getStory());
    }

    private void setupReplyListener() {
        replyView.setListener(this);
        loginSharedPreferences = LoginSharedPreferences.newInstance();
        if (loginSharedPreferences.isLoggedIn()) {
            replyFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReplyViewForStory();
                }
            });
        } else {
            replyFab.setActivated(false);
            replyFab.setVisibility(View.GONE);
        }
    }

    private void loadComments() {
        activity.getLoaderManager().initLoader(COMMENTS_LOADER, null, this);
    }

    private void showReplyViewForStory() {
        replyView.setStoryId(getStory().getId());
        showReplyView();
    }

    public void showReplyViewForComment(Long commentId) {
        replyView.setCommentId(commentId);
        replyView.setStoryId(getStory().getId());
        showReplyView();
    }

    private void showReplyView() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int centerX = (replyFab.getLeft() + replyFab.getRight()) / 2;
            int centerY = (replyFab.getTop() + replyFab.getBottom()) / 2;
            int finalRadius = Math.max(replyView.getWidth(), replyView.getHeight());
            mCircularReveal = ViewAnimationUtils.createCircularReveal(
                    replyView, centerX, centerY, 0, finalRadius);

            mCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mCircularReveal.removeListener(this);
                }
            });

            mCircularReveal.start();
        }

        replyFab.hide();
        replyView.setVisibility(View.VISIBLE);
        commentsView.setVisibility(View.GONE);
    }

    private void hideReplyView() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int cx = (replyFab.getLeft() + replyFab.getRight()) / 2;
            int cy = (replyFab.getTop() + replyFab.getBottom()) / 2;
            int initialRadius = replyView.getWidth();
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(replyView, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    replyView.clearAndHide();
                }
            });

            anim.start();
        } else {
            replyView.clearAndHide();
        }

        replyFab.show();
        commentsView.setVisibility(View.VISIBLE);
    }

    private void checkBookmarkMenuItem(MenuItem bookmarks) {
        bookmarks.setChecked(true);
        bookmarks.setIcon(R.drawable.ic_bookmark_white);
    }

    private void uncheckBookmarkMenuItem(MenuItem bookmarks) {
        bookmarks.setChecked(false);
        bookmarks.setIcon(R.drawable.ic_bookmark_outline_white);
    }

    private void showAddedBookmarkSnackbar(final CommentsOperator commentsOperator, final Story story) {
        snackbarView.showSnackBar(activity.getResources().getText(R.string.feed_snackbar_added_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarView.hideCrouton();
                        commentsOperator.onBookmarkUnselected();
                        showRemovedBookmarkSnackbar(commentsOperator, story);
                    }
                })
                .animating();
    }

    private void showRemovedBookmarkSnackbar(final CommentsOperator commentsOperator, final Story story) {
        snackbarView.showSnackBar(activity.getResources().getText(R.string.feed_snackbar_removed_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarView.hideCrouton();
                        commentsOperator.onBookmarkSelected();
                        showAddedBookmarkSnackbar(commentsOperator, story);
                    }
                })
                .animating();
    }

    public void showNotImplemented() {
        snackbarView.showSnackBar(activity.getResources().getText(R.string.feed_snackbar_not_implemented))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .animating();
    }

    public void showLoginExpired() {
        snackbarView.showSnackBar(activity.getResources().getText(R.string.login_expired_message))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withCustomTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.navigate().toLogin(null);
                    }
                }, R.string.feed_snackbar_text_sign_in)
                .animating();
    }

    public void showContentUpdating() {
        commentsView.startRefreshing();
    }

    @Override
    public void onReplyCancelled() {
        hideReplyView();
    }

    @Override
    public void onReplySuccessful() {
        hideReplyView();
        refreshListener.onRefresh();
    }

    @Override
    public void onLoginExpired() {
        hideReplyView();
        showLoginExpired();
    }

    @Override
    public void onCommentReplyAction(Long id) {
        showReplyViewForComment(id);
    }

    @Override
    public void onCommentVoteAction(Long id) {
        showNotImplemented();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri commentsUri = HNewsContract.CommentsEntry.buildCommentsUri();

        return new CursorLoader(
                activity,
                commentsUri,
                HNewsContract.CommentsEntry.COMMENT_COLUMNS,
                HNewsContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(getStory().getId())},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        commentsView.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        commentsView.swapCursor(null);
    }

    public void hideRefreshAnimation() {
        commentsView.stopRefreshing();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_bookmark:
                if (item.isChecked()) {
                    onBookmarkUnselected(item);
                } else {
                    onBookmarkSelected(item);
                }
                return true;
            case android.R.id.home:
                activity.finish();
                return true;
            default:
                return false;
        }

    }

    public void onRefresh(boolean isOnline) {
        if (isOnline) {
            showContentUpdating();
        } else {
            hideRefreshAnimation();
        }
    }

    public void onBackPressed() {
        if (inReplyMode()) {
            hideReplyView();
        } else {
            activity.supportFinishAfterTransition();
        }
    }

    public boolean inReplyMode() {
        return replyView.getVisibility() == View.VISIBLE;
    }
}
