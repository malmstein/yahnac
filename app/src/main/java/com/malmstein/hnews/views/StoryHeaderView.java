package com.malmstein.hnews.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Story;
import com.novoda.notils.caster.Views;

public class StoryHeaderView extends LinearLayout {

    private TextView text;
    private TextView author;
    private TextView when;

    public StoryHeaderView(Context context) {
        super(context);
    }

    public StoryHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoryHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StoryHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.view_story_comment_header, this, true);

        text = Views.findById(this, R.id.story_title);
        author = Views.findById(this, R.id.story_by);
        when = Views.findById(this, R.id.story_when);
    }

    public void updateWith(Story story){
        text.setText(Html.fromHtml(story.getTitle()));
        author.setText(story.getSubmitter());
        when.setText(story.getTimeAgo());
    }
}
