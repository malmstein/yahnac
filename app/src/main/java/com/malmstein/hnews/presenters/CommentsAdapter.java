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

public class CommentsAdapter extends CursorRecyclerAdapter<CommentsAdapter.ViewHolder> {

    public CommentsAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        Comment comment = Comment.from(cursor);
        holder.text.setText(Html.fromHtml(comment.getText()));
        holder.text.setMovementMethod(LinkMovementMethod.getInstance());
        holder.author.setText(Html.fromHtml(comment.getBy()));
        holder.when.setText(Html.fromHtml(comment.getTimeText()));
        holder.root.setPadding(comment.getLevel(), 0, 0, 0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final TextView author;
        public final TextView when;
        public final View root;

        public ViewHolder(View view) {
            super(view);
            text = Views.findById(view, R.id.comment_text);
            author = Views.findById(view, R.id.comment_by);
            when = Views.findById(view, R.id.comment_when);
            root = Views.findById(view, R.id.comment_root);
        }
    }

}