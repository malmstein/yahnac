package com.malmstein.yahnac.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.base.TimeAgo;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.updater.LoginSharedPreferences;
import com.novoda.notils.caster.Views;

public class StoryHeaderView extends LinearLayout {

    private LoginSharedPreferences loginSharedPreferences;
    private TextView text;
    private TextView author;
    private TextView when;
    private TextView comments;
    private View reply;

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

        this.loginSharedPreferences = LoginSharedPreferences.newInstance();
        LayoutInflater.from(getContext()).inflate(R.layout.view_story_comment_header, this, true);

        text = Views.findById(this, R.id.story_title);
        author = Views.findById(this, R.id.story_by);
        when = Views.findById(this, R.id.story_when);
        comments = Views.findById(this, R.id.story_comments);
        reply = Views.findById(this, R.id.story_reply_action);
    }

    public void updateWith(Story story, final Listener listener) {
        TimeAgo timeAgo = new TimeAgo(getContext().getResources());
        text.setText(Html.fromHtml(story.getTitle()));
        author.setText(getResources().getString(R.string.story_by, story.getSubmitter()));
        when.setText(timeAgo.timeAgo(story.getTimeAgo()));
        comments.setText(getResources().getQuantityString(R.plurals.story_comments, story.getComments(), story.getComments()));

        if (loginSharedPreferences.isLoggedIn()) {
            reply.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReplyAction();
                }
            });
        } else {
            reply.setVisibility(View.GONE);
        }
    }

    public interface Listener {
        void onReplyAction();
    }

}
