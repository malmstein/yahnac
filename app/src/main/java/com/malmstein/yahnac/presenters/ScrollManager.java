package com.malmstein.yahnac.presenters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

import com.malmstein.yahnac.base.DeveloperError;

public class ScrollManager extends RecyclerView.OnScrollListener {

    private final Listener listener;

    public ScrollManager(Listener listener) {
        if (listener == null) {
            throw new DeveloperError("You need to asign a Listener here dude!");
        }
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
            listener.onLoadMoreItems();
        }
    }

    public interface Listener {
        void onLoadMoreItems();
    }

}