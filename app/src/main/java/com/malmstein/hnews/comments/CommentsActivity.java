package com.malmstein.hnews.comments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.views.StoryHeaderView;
import com.novoda.notils.caster.Views;

public class CommentsActivity extends HNewsActivity {

    private StoryHeaderView storyHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        storyHeaderView = Views.findById(this, R.id.story_header_view);

        setupSubActivityWithTitle();
        setupStoryHeader();

        if (findCommentsFragment() == null) {
            Story story = (Story) getIntent().getExtras().getSerializable(CommentsFragment.ARG_STORY);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.comments_fragment_root, CommentsFragment.from(story), CommentsFragment.TAG).commit();
        }
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
}
