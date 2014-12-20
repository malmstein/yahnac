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
import com.malmstein.hnews.presenters.NewsAdapter;
import com.malmstein.hnews.sync.HNewsSyncAdapter;
import com.malmstein.hnews.views.DelegatedSwipeRefreshLayout;
import com.malmstein.hnews.views.ViewDelegate;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

import static com.malmstein.hnews.data.HNewsContract.ItemEntry;
import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class TopStoriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 0;

    private ListView mNewsListView;
    private NewsAdapter mNewsAdapter;
    private Listener listener;

    private DelegatedSwipeRefreshLayout refreshLayout;
    private int refreshViewOffset;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_stories, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        mNewsListView = (ListView) rootView.findViewById(R.id.listview_news);

        setupRefreshLayout();
        setupStoriesList();

        return rootView;
    }

    private void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
        refreshViewOffset = getResources().getDimensionPixelSize(R.dimen.feed_refresh_top_padding);
        refreshLayout.setProgressViewOffset(false, 0, refreshViewOffset);
    }

    private void setupStoriesList() {
        mNewsAdapter = new NewsAdapter(getActivity(), null, 0, listener);
        mNewsListView.setAdapter(mNewsAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri storyNewsUri = ItemEntry.buildItemsUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                STORY_COLUMNS,
                ItemEntry.COLUMN_TYPE + " = ?",
                new String[]{Item.TYPE.story.name()},
                ItemEntry.COLUMN_INSERTED + " ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNewsAdapter.swapCursor(data);
        stopRefreshing();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
        stopRefreshing();
    }

    @Override
    public void onRefresh() {
        HNewsSyncAdapter.syncImmediately(getActivity());
        updateProgressbar(true);
    }

    private void updateProgressbar(final boolean visible) {
        refreshLayout.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(visible);
            }
        });
        Log.w("Refreshing: " + visible);
    }

    private void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(mNewsListView, -1);
    }

    public interface Listener {
        void onShareClicked(Intent shareIntent);

        void onCommentsClicked();

        void onContentClicked(Long internalId);
    }

}
