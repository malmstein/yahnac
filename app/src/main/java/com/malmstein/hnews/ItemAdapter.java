package com.malmstein.hnews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public static ViewHolder newInstance(View parent) {
            TextView title = (TextView) parent.findViewById(R.id.item_text);
            return new ViewHolder(parent, title);
        }

        public ViewHolder(View parent, TextView title) {
            super(parent);
            this.mTextView = title;
        }

        public void setTitle(String title) {
            mTextView.setText(title);
        }
    }

    public ItemAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_listitem, parent, false);
        return ViewHolder.newInstance(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mDataset[position];
        holder.setTitle(title);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}