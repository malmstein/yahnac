package com.malmstein.hnews.presenters;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Comment;

public class CommentsAdapter extends CursorAdapter {

    public CommentsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        Comment comment = Comment.from(cursor);
        holder.text.setText(Html.fromHtml(comment.getText()));
        holder.text.setMovementMethod(LinkMovementMethod.getInstance());
        holder.author.setText(Html.fromHtml(comment.getBy()));
        holder.when.setText(Html.fromHtml(comment.getTimeText()));
        view.setPadding(comment.getLevel(), 0, 0, 0);
    }

    public static class ViewHolder {
        public final TextView text;
        public final TextView author;
        public final TextView when;

        public ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.comment_text);
            author = (TextView) view.findViewById(R.id.comment_by);
            when = (TextView) view.findViewById(R.id.comment_when);
        }
    }
}