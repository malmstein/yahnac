package com.malmstein.hnews.comments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Story;

public class CommentsActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setupSubActivityWithTitle();

        if (findCommentsFragment() == null) {
            Story story = (Story) getIntent().getExtras().getSerializable(CommentFragment.ARG_STORY);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.comments_fragment_root, CommentFragment.from(story), CommentFragment.TAG).commit();
        }
    }

    private CommentFragment findCommentsFragment() {
        return (CommentFragment) getSupportFragmentManager().findFragmentByTag(CommentFragment.TAG);
    }

}
