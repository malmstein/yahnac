package com.malmstein.yahnac.views.recyclerview.adapter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.injection.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.stories.StoryListener;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final StoryListener listener;
    private List<Story> items;

    public RecyclerViewAdapter(Cursor cursor, StoryListener listener) {
        items = new ArrayList<>();
        if (cursor != null) {
            for (int i = 0; cursor.moveToNext(); i++) {
                add(Story.fromBookmark(cursor), i);
            }
        }
        this.listener = listener;
    }

    public void swapCursor(Cursor cursor) {
        removeAll();
        if (cursor != null) {
            for (int i = 0; cursor.moveToNext(); i++) {
                add(Story.fromBookmark(cursor), i);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bookmark_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Story story = items.get(position);

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
                DataPersister persister = Inject.dataPersister();
                if (story.isBookmark()) {
                    persister.removeBookmark(story);
                    listener.onBookmarkRemoved(story);
                } else {
                    persister.addBookmark(story);
                    listener.onBookmarkAdded(story);
                }
            }
        });

        if (story.isStoryAJob()) {
            holder.user.setVisibility(View.GONE);
            holder.comments_action.setVisibility(View.GONE);
        } else {
            holder.user.setText(holder.user.getResources().getString(R.string.story_by, story.getSubmitter()));
            holder.comments_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (story.isJob()) {
                        listener.onContentClicked(story);
                    } else {
                        listener.onCommentsClicked(story);
                    }
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
    public int getItemCount() {
        return items.size();
    }

    private void add(Story item, int position) {
        items.add(position, item);
        notifyDataSetChanged();
    }

    private void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    private Intent createShareIntent(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView user;
        public final View card;
        public final View external_action;
        public final View share_action;
        public final View comments_action;
        public final View bookmark_action;
        public final TextView domain;

        public ViewHolder(View view) {
            super(view);
            title = Views.findById(view, R.id.article_title);
            user = Views.findById(view, R.id.article_user);
            card = Views.findById(view, R.id.article_card_root);
            external_action = Views.findById(view, R.id.article_external_action);
            share_action = Views.findById(view, R.id.article_share_action);
            comments_action = Views.findById(view, R.id.article_comments_action);
            bookmark_action = Views.findById(view, R.id.article_bookmark_action);
            domain = Views.findById(view, R.id.article_domain);
        }
    }
}
