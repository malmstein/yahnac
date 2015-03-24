package com.malmstein.yahnac.views.quickreturn;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.malmstein.yahnac.stories.StoryListener;

public class RecyclerViewQuickReturnHinter extends RecyclerView.OnScrollListener {

    private final int longScrollSlop;
    private final int smallScrollSlop;
    private final StoryListener listener;

    public RecyclerViewQuickReturnHinter(int longScrollSlop, int smallScrollSlop, StoryListener listener) {
        this.longScrollSlop = longScrollSlop;
        this.smallScrollSlop = smallScrollSlop;
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int deltaX, int deltaY) {
        int absoluteDeltaY = Math.abs(deltaY);
        boolean scrollingUp = isScrollingUp(deltaY);
        if (isShowingFirstItem(recyclerView)) {
            if (scrollingUp) {
                listener.onQuickReturnVisibilityChangeHint(true);
            }
        } else {
            if (hasScrolledALot(absoluteDeltaY)) {
                listener.onQuickReturnVisibilityChangeHint(scrollingUp);
            } else if (hasScrolledDownEnoughToHide(deltaY)) {
                listener.onQuickReturnVisibilityChangeHint(false);
            }
        }
    }

    private boolean isScrollingUp(int deltaY) {
        return deltaY < 0;
    }

    private boolean isShowingFirstItem(RecyclerView recyclerView) {
        View firstChild = recyclerView.getChildAt(0);
        if (firstChild == null) {
            return false;
        }
        return recyclerView.getChildPosition(firstChild) == 0;
    }

    private boolean hasScrolledALot(int absoluteDeltaY) {
        return absoluteDeltaY > longScrollSlop;
    }

    private boolean hasScrolledDownEnoughToHide(int deltaY) {
        return deltaY > smallScrollSlop;
    }

}
