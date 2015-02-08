package com.malmstein.hnews.presenters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.hnews.R;

public class MyAdapter extends CursorRecyclerAdapter<MyAdapter.ViewHolder> {

    public MyAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {

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

        public ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.comment_text);
            author = (TextView) view.findViewById(R.id.comment_by);
            when = (TextView) view.findViewById(R.id.comment_when);
        }
    }

}