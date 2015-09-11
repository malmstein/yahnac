package com.malmstein.yahnac.comments;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.DelegatedSwipeRefreshLayout;
import com.malmstein.yahnac.views.ViewDelegate;
import com.malmstein.yahnac.views.recyclerview.adapter.CursorRecyclerAdapter;
import com.malmstein.yahnac.views.recyclerview.decorators.CommentsRecyclerItemDecoration;
import com.novoda.notils.caster.Views;

public class CommentsView extends FrameLayout implements ViewDelegate, SwipeRefreshLayout.OnRefreshListener {

    private DelegatedSwipeRefreshLayout refreshLayout;
    private RecyclerView commentsList;
    private CursorRecyclerAdapter commentsAdapter;
    private RecyclerView.LayoutManager commentsLayoutManager;

    private Story story;

    public CommentsView(Context context) {
        super(context);
    }

    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommentsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.view_comments, this, true);

        refreshLayout = Views.findById(this, R.id.feed_refresh);
        commentsList = Views.findById(this, R.id.list_comments);

    }

    public void setupWith(CommentsAdapter.Listener adapterListener, SwipeRefreshLayout.OnRefreshListener refreshListener, Story story) {
        this.story = story;

        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(refreshListener);
        refreshLayout.setViewDelegate(this);

        commentsList.setHasFixedSize(true);
        commentsLayoutManager = new LinearLayoutManager(getContext());
        commentsList.addItemDecoration(createItemDecoration(getResources()));
        commentsList.setLayoutManager(commentsLayoutManager);

        commentsAdapter = new CommentsAdapter(story.getType(), null, adapterListener);
        commentsList.setAdapter(commentsAdapter);
    }

    public void startRefreshing() {
        refreshLayout.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    private CommentsRecyclerItemDecoration createItemDecoration(Resources resources) {
        int verticalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_divider_height);
        int horizontalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_padding_infra_spans);
        return new CommentsRecyclerItemDecoration(verticalItemSpacingInPx, horizontalItemSpacingInPx);
    }

    protected void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    public void swapCursor(Cursor data) {
        commentsAdapter.swapCursor(data);
        stopRefreshing();
    }

    @Override
    public void onRefresh() {
        startRefreshing();
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(commentsList, -1);
    }

}
