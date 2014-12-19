package com.malmstein.hnews.presenters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Story;

public class NewsAdapter extends CursorAdapter {

    public NewsAdapter(Context context, Cursor c, int flags) {
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
        Story story = Story.from(cursor);
        holder.title.setText(story.getTitle());
    }

    public static class ViewHolder {
        public final TextView title;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.article_title);
        }
    }
}