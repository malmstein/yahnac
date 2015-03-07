package com.malmstein.yahnac.stories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;

import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.ViewDelegate;

import static com.malmstein.yahnac.data.HNewsContract.ItemEntry;

public class BookmarksFragment extends StoryFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableRefresh();
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    protected String getOrder() {
        return HNewsContract.ItemEntry.COLUMN_TIMESTAMP + " DESC";
    }

    @Override
    protected Story.TYPE getType() {
        return Story.TYPE.show;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyNewsUri = ItemEntry.buildBookmarksUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                HNewsContract.BOOKMARK_COLUMNS,
                null,
                null,
                getOrder());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        storiesAdapter.swapCursor(data);
        stopRefreshing();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        storiesAdapter.swapCursor(null);
        stopRefreshing();
    }

}
