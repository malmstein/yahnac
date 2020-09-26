package com.malmstein.yahnac.stories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.ViewDelegate;

public class JobsHNFragment extends StoryFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 1004;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    @Override
    protected Story.FILTER getType() {
        return Story.FILTER.jobs;
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
                new String[]{Story.FILTER.jobs.name()},
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
