package com.malmstein.hnews.stories;

import android.app.Activity;
import android.content.Intent;
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

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Item;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.presenters.TopStoriesAdapter;
import com.malmstein.hnews.sync.HNewsSyncAdapter;
import com.malmstein.hnews.views.DelegatedSwipeRefreshLayout;
import com.malmstein.hnews.views.ViewDelegate;
import com.novoda.notils.caster.Views;

import static com.malmstein.hnews.data.HNewsContract.ItemEntry;
import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class TopStoriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 0;

    private static final String SELECTED_KEY = "selected_position";

    private ListView mNewsListView;
    private TopStoriesAdapter mTopStoriesAdapter;
    private Listener listener;

    private DelegatedSwipeRefreshLayout refreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HNewsSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_stories, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        mNewsListView = (ListView) rootView.findViewById(R.id.listview_news);
        mNewsListView.setEmptyView(rootView.findViewById(R.id.feed_empty_placeholder));
        setupRefreshLayout();
        setupStoriesList();

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mTopStoriesAdapter.setSelectedPosition(savedInstanceState.getInt(SELECTED_KEY));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int position = mTopStoriesAdapter.getSelectedPosition();
        if (position != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, position);
        }
        super.onSaveInstanceState(outState);
    }

    private void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
    }

    private void setupStoriesList() {
        mTopStoriesAdapter = new TopStoriesAdapter(getActivity(), null, 0, listener);
        mNewsListView.setAdapter(mTopStoriesAdapter);
    }

    private void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyNewsUri = ItemEntry.buildStoriesUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                STORY_COLUMNS,
                ItemEntry.COLUMN_TYPE + " = ?",
                new String[]{Item.TYPE.story.name()},
                ItemEntry.COLUMN_ITEM_ORDER + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        stopRefreshing();
        mTopStoriesAdapter.swapCursor(data);
        int position = mTopStoriesAdapter.getSelectedPosition();
        if (position != ListView.INVALID_POSITION) {
            mNewsListView.smoothScrollToPosition(position);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stopRefreshing();
        mTopStoriesAdapter.swapCursor(null);
        stopRefreshing();
    }

    @Override
    public void onRefresh() {
        HNewsSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(mNewsListView, -1);
    }

    public interface Listener {
        void onShareClicked(Intent shareIntent);

        void onCommentsClicked(Story story);

        void onContentClicked(Story story);
    }

}
