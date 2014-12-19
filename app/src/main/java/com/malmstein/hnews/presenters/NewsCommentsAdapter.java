package com.malmstein.hnews.presenters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Comment;

public class NewsCommentsAdapter extends CursorAdapter {

    public NewsCommentsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_article_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        Comment comment = Comment.from(cursor);
        holder.text.setText(comment.getText());
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final TextView text;

        public ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.title);
        }
    }
}