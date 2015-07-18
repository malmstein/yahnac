package com.malmstein.yahnac.stories;

import android.content.Intent;
import android.view.View;

import com.malmstein.yahnac.model.Story;

public interface StoryListener {

    void onShareClicked(Intent shareIntent);

    void onCommentsClicked(View v, Story story);

    void onCommentsClicked(Story story);

    void onContentClicked(Story story);

    void onExternalLinkClicked(Story story);

    void onBookmarkAdded(Story story);

    void onBookmarkRemoved(Story story);

    void onStoryVoteClicked(Story story);

}
