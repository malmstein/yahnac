package com.malmstein.hnews.presenters;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.stories.StoryListener;

public class StoriesAdapter extends CursorRecyclerAdapter<StoriesAdapter.ViewHolder> {

    private final StoryListener listener;

    public StoriesAdapter(Cursor cursor, StoryListener listener) {
        super(cursor);
        this.listener = listener;
    }

    private Intent createShareIntent(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        final Story story = Story.from(cursor);

        holder.title.setText(story.getTitle());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContentClicked(story);
            }
        });

        holder.share_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShareClicked(createShareIntent(story.getUrl()));
            }
        });

        if (story.isStoryAJob()) {
            holder.user.setVisibility(View.GONE);
            holder.timeAgo.setVisibility(View.GONE);
            holder.score.setVisibility(View.GONE);
            holder.comments_action.setVisibility(View.GONE);
        } else {
            holder.user.setText(holder.user.getResources().getString(R.string.story_by, story.getSubmitter()));
            holder.timeAgo.setText(story.getTimeAgo());
            holder.score.setText(holder.score.getResources().getString(R.string.story_points, story.getScore()));
            holder.comments_action.setText(holder.user.getResources().getString(R.string.story_comments, story.getComments()));
            holder.comments_action.setText(story.getComments());
            holder.comments_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentsClicked(v, story);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_story_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView user;
        public final TextView timeAgo;
        public final TextView score;
        public final View card;
        public final TextView share_action;
        public final TextView comments_action;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.article_title);
            user = (TextView) view.findViewById(R.id.article_user);
            timeAgo = (TextView) view.findViewById(R.id.article_time);
            score = (TextView) view.findViewById(R.id.article_score);
            card = view.findViewById(R.id.article_card_root);
            share_action = (TextView) view.findViewById(R.id.article_share_action);
            comments_action = (TextView) view.findViewById(R.id.article_comments_action);
        }
    }

}