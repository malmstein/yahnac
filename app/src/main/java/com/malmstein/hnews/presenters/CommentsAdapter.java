package com.malmstein.hnews.presenters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Comment;
import com.novoda.notils.caster.Views;

public class CommentsAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {

    private static int TYPE_HEADER = 0;
    private static int TYPE_COMMENT = 1;

    public CommentsAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEADER;
        } else {
            return TYPE_COMMENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return createHeaderHolder(parent);
        } else {
            return createCommentHolder(parent);
        }
    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
        Comment comment = Comment.from(cursor);

        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).bind(comment);
        } else {
            ((HeaderViewHolder) holder).bind(comment);
        }

    }

    private RecyclerView.ViewHolder createHeaderHolder(ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment_header, parent, false);
        HeaderViewHolder vh = new HeaderViewHolder(v);
        return vh;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final TextView text;

        public HeaderViewHolder(View view) {
            super(view);
            text = Views.findById(view, R.id.comment_header_text);
        }

        public void bind(Comment comment){
            text.setText(Html.fromHtml(comment.getText()));
        }
    }

    private RecyclerView.ViewHolder createCommentHolder(ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        CommentViewHolder vh = new CommentViewHolder(v);
        return vh;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final TextView author;
        public final TextView when;
        public final View root;

        public CommentViewHolder(View view) {
            super(view);
            text = Views.findById(view, R.id.comment_text);
            author = Views.findById(view, R.id.comment_by);
            when = Views.findById(view, R.id.comment_when);
            root = Views.findById(view, R.id.comment_root);
        }

        public void bind(Comment comment) {
            text.setText(Html.fromHtml(comment.getText()));
            text.setMovementMethod(LinkMovementMethod.getInstance());
            author.setText(comment.getBy());
            when.setText(comment.getTimeText());
            root.setPadding(comment.getLevel(), 0, 0, 0);
        }
    }

}