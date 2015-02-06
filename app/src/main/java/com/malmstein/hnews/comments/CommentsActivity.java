package com.malmstein.hnews.comments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.views.RevealBackgroundView;
import com.novoda.notils.caster.Views;

public class CommentsActivity extends HNewsActivity implements RevealBackgroundView.OnStateChangeListener {

    private RevealBackgroundView commentsRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setupSubActivityWithTitle();

        setupRevealBackground(savedInstanceState);

        if (findCommentsFragment() == null) {
            Long storyId = getIntent().getExtras().getLong(CommentFragment.ARG_STORY_ID);
            String title = getIntent().getExtras().getString(CommentFragment.ARG_STORY_TITLE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.comments_fragment_root, CommentFragment.from(storyId, title), CommentFragment.TAG).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    private CommentFragment findCommentsFragment() {
        return (CommentFragment) getSupportFragmentManager().findFragmentByTag(CommentFragment.TAG);
    }

    private int[] getStartingPoint() {
        if (getIntent().hasExtra(CommentFragment.ARG_LOCATION)) {
            return getIntent().getExtras().getIntArray(CommentFragment.ARG_LOCATION);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        commentsRoot = Views.findById(this, R.id.comments_root);
        commentsRoot.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            commentsRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    commentsRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    commentsRoot.startFromLocation(getStartingPoint());
                    return true;
                }
            });
        } else {
            commentsRoot.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_RESTORED == state) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        commentsRoot.endToOriginalLocation(getStartingPoint());
    }
}
