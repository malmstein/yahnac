package com.malmstein.yahnac.views.recyclerview.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FeedRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalItemSpacingInPx;
    private final int horizontalItemSpacingInPx;

    public FeedRecyclerItemDecoration(int verticalItemSpacingInPx, int horizontalItemSpacingInPx) {
        this.verticalItemSpacingInPx = verticalItemSpacingInPx;
        this.horizontalItemSpacingInPx = horizontalItemSpacingInPx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewPosition();
        int childCount = parent.getAdapter().getItemCount();

        int left = horizontalItemSpacingInPx;
        int right = horizontalItemSpacingInPx;

        int top = getItemTopSpacing(itemPosition);
        int bottom = getItemBottomSpacing(itemPosition, childCount);
        outRect.set(left, top, right, bottom);
    }

    private int getItemTopSpacing(int itemPosition) {
        if (isFirstItem(itemPosition)) {
            return verticalItemSpacingInPx;
        }
        return verticalItemSpacingInPx / 2;
    }

    private boolean isFirstItem(int itemPosition) {
        return itemPosition == 0;
    }

    private int getItemBottomSpacing(int itemPosition, int childCount) {
        if (isLastItem(itemPosition, childCount)) {
            return verticalItemSpacingInPx;
        }
        return verticalItemSpacingInPx / 2;
    }

    private boolean isLastItem(int itemPosition, int childCount) {
        return itemPosition == childCount - 1;
    }

}
