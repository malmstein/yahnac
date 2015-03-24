package com.malmstein.yahnac.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.novoda.notils.logger.simple.Log;

public abstract class AsyncRecyclerView extends RecyclerView {

    public AsyncRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AsyncRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncRecyclerView(Context context) {
        super(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        setHasFixedSize(true);
        addItemDecoration(createItemDecoration(resources));
        setLayoutManager(createLayoutManager(resources));
        setChildrenDrawingOrderEnabled(true);
    }

    protected abstract ItemDecoration createItemDecoration(Resources resources);

    protected abstract LayoutManager createLayoutManager(Resources resources);

    public int getItemCount() {
        Adapter adapter = getAdapter();
        if (adapter == null) {
            return 0;
        }
        return adapter.getItemCount();
    }

    public boolean isScrolledDown() {
        int firstVisiblePosition = getFirstVisiblePosition();
        if (firstVisiblePosition > 0) {
            return true;
        }
        LayoutManager layoutManager = getLayoutManager();
        View firstVisibleView = layoutManager.findViewByPosition(firstVisiblePosition);
        if (firstVisibleView == null) {
            return false;
        }
        return layoutManager.getDecoratedTop(firstVisibleView) < layoutManager.getPaddingTop();
    }

    public void scrollToTop() {
        smoothScrollToPosition(0);
    }

    public void returnToTop() {
        getLayoutManager().scrollToPosition(0);
    }

    public int getFirstVisiblePosition() {
        View firstChild = getChildAt(0);
        return getChildPosition(firstChild);
    }

    public int getLastVisiblePosition() {
        return getFirstVisiblePosition() + getChildCount() - 1;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int lastItemIndex = childCount - 1;
        return lastItemIndex - i;
    }

    public boolean isFirstItemInViewport() {
        int firstVisiblePosition = getFirstVisiblePosition();
        if (firstVisiblePosition > 0) {
            return false;
        }
        View firstVisibleView = findViewByAdapterPosition(firstVisiblePosition);
        if (firstVisibleView == null) {
            return false;
        }
        return viewportIsShowing(firstVisibleView);
    }

    private View findViewByAdapterPosition(int position) {
        return getLayoutManager().findViewByPosition(position);
    }

    private boolean viewportIsShowing(View view) {
        return getDecoratedBottom(view) >= 0;
    }

    private int getDecoratedBottom(View firstVisibleView) {
        return getLayoutManager().getDecoratedBottom(firstVisibleView);
    }

    public void scrollToTopWithOffset(int px) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(0, -px);
        } else {
            Log.w("Only StaggeredGridLayoutManager2 supports scrolling to position with offset!");
        }
    }

}
