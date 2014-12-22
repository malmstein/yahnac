package com.malmstein.hnews.comments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.R;
import com.malmstein.hnews.inject.Inject;
import com.malmstein.hnews.presenters.NewsCommentsAdapter;
import com.malmstein.hnews.views.DelegatedSwipeRefreshLayout;
import com.malmstein.hnews.views.ViewDelegate;
import com.novoda.notils.caster.Views;

import static com.malmstein.hnews.data.HNewsContract.COMMENT_COLUMNS;
import static com.malmstein.hnews.data.HNewsContract.ItemEntry;

public class CommentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int COMMENTS_LOADER = 0;
    public static final String ARG_STORY_ID = BuildConfig.APPLICATION_ID + ".ARG_COMMENT_STORY_ID";

    private DelegatedSwipeRefreshLayout refreshLayout;
    private ListView commentsListView;
    private NewsCommentsAdapter commentsAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COMMENTS_LOADER, null, this);

        CommentsProvider commentsProvider = Inject.commentsProvider();
        commentsProvider.fetch(getArgStoryId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        commentsListView = (ListView) rootView.findViewById(R.id.listview_comments);
        commentsAdapter = new NewsCommentsAdapter(getActivity(), null, 0);
        commentsListView.setAdapter(commentsAdapter);

        setupRefreshLayout();
        setupCommentsList();

        return rootView;
    }

    private void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
    }

    private void setupCommentsList() {
        commentsAdapter = new NewsCommentsAdapter(getActivity(), null, 0);
        commentsListView.setAdapter(commentsAdapter);
    }

    private Long getArgStoryId(){
        return getActivity().getIntent().getLongExtra(ARG_STORY_ID, 0);
    }

    private void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri commentsUri = ItemEntry.buildCommentsUri();

        return new CursorLoader(
                getActivity(),
                commentsUri,
                COMMENT_COLUMNS,
                ItemEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(getArgStoryId())},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        commentsAdapter.swapCursor(data);
        stopRefreshing();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        commentsAdapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        CommentsProvider commentsProvider = Inject.commentsProvider();
        commentsProvider.fetch(getArgStoryId());
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(commentsListView, -1);
    }
}
