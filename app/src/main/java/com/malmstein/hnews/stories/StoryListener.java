package com.malmstein.hnews.stories;

import android.content.Intent;

import com.malmstein.hnews.model.Story;

public interface StoryListener {

    void onShareClicked(Intent shareIntent);

    void onCommentsClicked(Story story);

    void onContentClicked(Story story);

}
