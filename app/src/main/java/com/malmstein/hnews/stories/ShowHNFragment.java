package com.malmstein.hnews.stories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Item;
import com.malmstein.hnews.presenters.StoriesAdapter;
import com.malmstein.hnews.sync.HNewsSyncAdapter;
import com.malmstein.hnews.views.ViewDelegate;

import static com.malmstein.hnews.data.HNewsContract.ItemEntry;
import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class ShowHNFragment extends StoryFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_top_stories;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int position = storiesAdapter.getSelectedPosition();
        if (position != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, position);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected StoriesAdapter getStoriesAdapter(StoryListener listener) {
        return new StoriesAdapter(getActivity(), null, 0, listener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyNewsUri = ItemEntry.buildStoriesUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                STORY_COLUMNS,
                ItemEntry.COLUMN_TYPE + " = ?",
                new String[]{Item.TYPE.show.name()},
                ItemEntry.COLUMN_ITEM_ORDER + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        stopRefreshing();
        storiesAdapter.swapCursor(data);
        int position = storiesAdapter.getSelectedPosition();
        if (position != ListView.INVALID_POSITION) {
            storiesList.smoothScrollToPosition(position);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stopRefreshing();
        storiesAdapter.swapCursor(null);
        stopRefreshing();
    }

    @Override
    public void onRefreshDelegated() {
        HNewsSyncAdapter.syncImmediately(getActivity());
    }

}
