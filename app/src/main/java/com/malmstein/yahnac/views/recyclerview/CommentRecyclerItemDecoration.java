package com.malmstein.yahnac.views.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CommentRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    private final int verticalItemSpacingInPx;
    private final int horizontalItemSpacingInPx;

    public CommentRecyclerItemDecoration(Drawable divider, int verticalItemSpacingInPx, int horizontalItemSpacingInPx) {
        mDivider = divider;
        this.verticalItemSpacingInPx = verticalItemSpacingInPx;
        this.horizontalItemSpacingInPx = horizontalItemSpacingInPx;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.onDrawOver(c, parent, state);
            return;
        }

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();

            for (int i = 1; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int size = mDivider.getIntrinsicHeight();
                final int top = child.getTop() - params.topMargin;
                final int bottom = top + size;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else { //horizontal
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();

            for (int i = 1; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int size = mDivider.getIntrinsicWidth();
                final int left = child.getLeft() - params.leftMargin;
                final int right = left + size;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            return layoutManager.getOrientation();
        } else {
            throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
        }
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
