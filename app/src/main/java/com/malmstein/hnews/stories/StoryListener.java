package com.malmstein.hnews.stories;

import android.content.Intent;
import android.view.View;

import com.malmstein.hnews.model.Story;

public interface StoryListener {

    void onShareClicked(Intent shareIntent);

    void onCommentsClicked(View v, Story story);

    void onContentClicked(Story story);

}
