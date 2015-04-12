package com.malmstein.yahnac.presenters;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.base.TimeAgo;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.stories.StoryListener;
import com.novoda.notils.caster.Views;

public class StoriesAdapter extends CursorRecyclerAdapter<StoriesAdapter.ViewHolder> {

    private final StoryListener listener;
    private final TimeAgo timeAgo;

    public StoriesAdapter(Cursor cursor, StoryListener listener, TimeAgo timeAgo) {
        super(cursor);
        this.listener = listener;
        this.timeAgo = timeAgo;
    }

    private Intent createShareIntent(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

    @Override
    public void onBindViewHolderCursor(final ViewHolder holder, Cursor cursor) {
        final Story story = Story.from(cursor);

        holder.title.setTextColor(story.isRead() ?
                holder.title.getResources().getColor(R.color.grey) :
                holder.title.getResources().getColor(R.color.black));

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

        holder.bookmark_action.setSelected(story.isBookmark());
        holder.bookmark_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookmarkClicked(story);
            }
        });

        if (story.isStoryAJob()) {
            holder.user.setVisibility(View.GONE);
            holder.timeAgo.setVisibility(View.GONE);
            holder.score.setVisibility(View.GONE);
            holder.comments_action.setVisibility(View.GONE);
        } else {
            holder.user.setText(holder.user.getResources().getString(R.string.story_by, story.getSubmitter()));
            holder.timeAgo.setText(timeAgo.timeAgo(story.getTimeAgo()));
            holder.score.setText(holder.score.getResources().getString(R.string.story_points, story.getScore()));
            holder.comments_text.setText(holder.user.getResources().getQuantityString(R.plurals.story_comments, story.getComments(), story.getComments()));
            holder.comments_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentsClicked(holder.title, story);
                }
            });
        }

        if (story.isHackerNewsLocalItem()) {
            holder.external_action.setVisibility(View.GONE);
        } else {
            holder.external_action.setVisibility(View.VISIBLE);
            holder.external_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onExternalLinkClicked(story);
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
        public final View external_action;
        public final View share_action;
        public final View comments_action;
        public final View bookmark_action;
        public final TextView comments_text;

        public ViewHolder(View view) {
            super(view);
            title = Views.findById(view, R.id.article_title);
            user = Views.findById(view, R.id.article_user);
            timeAgo = Views.findById(view, R.id.article_time);
            score = Views.findById(view, R.id.article_score);
            card = Views.findById(view, R.id.article_card_root);
            external_action = Views.findById(view, R.id.article_external_action);
            share_action = Views.findById(view, R.id.article_share_action);
            bookmark_action = Views.findById(view, R.id.article_bookmark_action);
            comments_action = Views.findById(view, R.id.article_comments);
            comments_text = Views.findById(view, R.id.article_comments_label);
        }
    }

}