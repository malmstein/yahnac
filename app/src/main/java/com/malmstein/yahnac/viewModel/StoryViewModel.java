package com.malmstein.yahnac.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.malmstein.yahnac.model.Story;

public class StoryViewModel extends BaseObservable {

    private final Context context;
    private final Story story;

    public StoryViewModel(Context context, Story story) {
        this.context = context;
        this.story = story;
    }

    public String getTitle() {
        return story.getTitle();
    }

}
