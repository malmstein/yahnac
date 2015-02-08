package com.malmstein.hnews.presenters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.base.TimeAgo;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.stories.StoryListener;

public class StoriesCursorAdapter extends CursorAdapter {

    private int currentPosition;
    private final StoryListener listener;
    private TimeAgo timeAgo;

    public StoriesCursorAdapter(Context context, Cursor c, int flags, StoryListener listener) {
        super(context, c, flags);
        this.listener = listener;
        timeAgo = new TimeAgo(context.getResources());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_article_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        final Story story = Story.from(cursor);
        holder.title.setText(story.getTitle());
        holder.user.setText(context.getResources().getString(R.string.story_by, story.getBy()));
        holder.timeAgo.setText(story.getTimeAgo());
        holder.score.setText(context.getResources().getString(R.string.story_points, story.getScore()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = cursor.getPosition();
                listener.onContentClicked(story);
            }
        });
        holder.share_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShareClicked(createShareIntent(story.getUrl()));
            }
        });

        holder.comments_action.setText(story.getComments());
        holder.comments_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = cursor.getPosition();
                listener.onCommentsClicked(v, story);
                // start the new activity
            }
        });
//        }

    }

    private Intent createShareIntent(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

    public void setSelectedPosition(int position) {
        currentPosition = position;
    }

    public int getSelectedPosition() {
        return currentPosition;
    }

    public static class ViewHolder {
        public final TextView title;
        public final TextView user;
        public final TextView timeAgo;
        public final TextView score;
        public final View card;
        public final TextView share_action;
        public final TextView comments_action;

        public ViewHolder(View view) {
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