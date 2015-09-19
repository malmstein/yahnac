package com.malmstein.yahnac.views.recyclerview.decorators;

import android.database.Cursor;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.malmstein.yahnac.comments.CommentsAdapter;
import com.malmstein.yahnac.model.Comment;

public class CommentsRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalItemSpacingInPx;
    private final int horizontalItemSpacingInPx;

    public CommentsRecyclerItemDecoration(int verticalItemSpacingInPx, int horizontalItemSpacingInPx) {
        this.verticalItemSpacingInPx = verticalItemSpacingInPx;
        this.horizontalItemSpacingInPx = horizontalItemSpacingInPx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewAdapterPosition();

        CommentsAdapter adapter = (CommentsAdapter) parent.getAdapter();
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(itemPosition);

        Comment comment = Comment.from(cursor);
        int childCount = parent.getAdapter().getItemCount();
        int left = horizontalItemSpacingInPx + comment.getLevel();
        int right = horizontalItemSpacingInPx;

        int top = getItemTopSpacing(itemPosition);
        int bottom = getItemBottomSpacing(itemPosition, childCount);
        outRect.set(left, top, right, bottom);
    }

    private int getItemTopSpacing(int itemPosition) {
        if (isFirstItem(itemPosition)) {
            return verticalItemSpacingInPx;
        }
        return 0;
    }

    private boolean isFirstItem(int itemPosition) {
        return itemPosition == 0;
    }

    private int getItemBottomSpacing(int itemPosition, int childCount) {
        if (isLastItem(itemPosition, childCount)) {
            return verticalItemSpacingInPx;
        }
        return 0;
    }

    private boolean isLastItem(int itemPosition, int childCount) {
        return itemPosition == childCount - 1;
    }

}
