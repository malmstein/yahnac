package com.malmstein.hnews.comments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;

public class CommentsActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setupSubActivityWithTitle();

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

}
