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

public class ShowHNFragment extends StoryFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 1003;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    @Override
    protected Story.FILTER getType() {
        return Story.FILTER.show;
    }

    protected String getOrder() {
        return HNewsContract.StoryEntry.RANK + " ASC" +
                ", " + HNewsContract.StoryEntry.TIMESTAMP + " DESC";
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyNewsUri = HNewsContract.StoryEntry.buildStoriesUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                HNewsContract.StoryEntry.STORY_COLUMNS,
                HNewsContract.StoryEntry.FILTER + " = ?",
                new String[]{Story.FILTER.show.name()},
                getOrder());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        storiesAdapter.swapCursor(data);
        stopRefreshing();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}
