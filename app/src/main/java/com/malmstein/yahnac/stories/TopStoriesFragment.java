package com.malmstein.yahnac.stories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;

import com.malmstein.yahnac.base.DeveloperError;
import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.ViewDelegate;

public class TopStoriesFragment extends StoryFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 0;

    public static TopStoriesFragment from(QUERY query) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("query", query);
        TopStoriesFragment fragment = new TopStoriesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    public Story.TYPE getType() {
        QUERY query = (QUERY) getArguments().get("query");
        switch (query) {
            case top:
                return Story.TYPE.top_story;
            case newest:
                return Story.TYPE.top_story;
            case best:
                return Story.TYPE.top_story;
            default:
                new DeveloperError("Bad Query type");
                return null;
        }
    }

    protected String getOrder() {
        QUERY query = (QUERY) getArguments().get("query");
        switch (query) {
            case top:
                return HNewsContract.StoryEntry.RANK + " ASC" +
                        ", " + HNewsContract.StoryEntry.TIMESTAMP + " DESC";
            case newest:
                return HNewsContract.StoryEntry.TIMESTAMP + " DESC";
            case best:
                return HNewsContract.StoryEntry.SCORE + " DESC" +
                        ", " + HNewsContract.StoryEntry.TIMESTAMP + " DESC";
            default:
                new DeveloperError("Bad Query type");
                return null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyNewsUri = HNewsContract.StoryEntry.buildStoriesUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                HNewsContract.StoryEntry.STORY_COLUMNS,
                HNewsContract.StoryEntry.TYPE + " = ?",
                new String[]{getType().name()},
                getOrder());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        storiesAdapter.swapCursor(data);
        stopRefreshing();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public enum QUERY {
        top,
        newest,
        best
    }

}
