package com.malmstein.yahnac.comments;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.DelegatedSwipeRefreshLayout;
import com.malmstein.yahnac.views.ViewDelegate;
import com.malmstein.yahnac.views.recyclerview.adapter.CursorRecyclerAdapter;
import com.malmstein.yahnac.views.recyclerview.decorators.CommentsRecyclerItemDecoration;
import com.novoda.notils.caster.Views;

import rx.Subscription;

public class CommentsView extends FrameLayout implements ViewDelegate, SwipeRefreshLayout.OnRefreshListener {

    private DelegatedSwipeRefreshLayout refreshLayout;
    private RecyclerView commentsList;
    private CursorRecyclerAdapter commentsAdapter;
    private RecyclerView.LayoutManager commentsLayoutManager;

    private CommentsAdapter.Listener listener;

    private Subscription subscription;
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

        refreshLayout = Views.findById(this, R.id.feed_refresh);
        commentsList = Views.findById(this, R.id.list_comments);

        setupRefreshLayout();
    }

    public void setupWith(CommentsAdapter.Listener listener, Story story) {
        this.listener = listener;
        this.story = story;

        commentsList.setHasFixedSize(true);
        commentsLayoutManager = new LinearLayoutManager(getContext());
        commentsList.addItemDecoration(createItemDecoration(getResources()));
        commentsList.setLayoutManager(commentsLayoutManager);

        commentsAdapter = new CommentsAdapter(story.getType(), null, listener);
        commentsList.setAdapter(commentsAdapter);
    }

    private void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
    }

    private CommentsRecyclerItemDecoration createItemDecoration(Resources resources) {
        int verticalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_divider_height);
        int horizontalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_padding_infra_spans);
        return new CommentsRecyclerItemDecoration(verticalItemSpacingInPx, horizontalItemSpacingInPx);
    }

    @Override
    public void onRefresh() {
        retrieveComments();
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(commentsList, -1);
    }

}
