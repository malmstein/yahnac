package com.malmstein.hnews.stories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;

import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.views.ViewDelegate;

import static com.malmstein.hnews.data.HNewsContract.ItemEntry;
import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class TopStoriesFragment extends StoryFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private static final int STORY_LOADER = 0;

    public enum QUERY {
        top,
        newest,
        best
    }

    public static TopStoriesFragment from(QUERY query){
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

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_top_stories;
    }

    public Story.TYPE getType(){
        QUERY query = (QUERY) getArguments().get("query");
        switch (query){
            case top:
                return Story.TYPE.top_story;
            case newest:
                return Story.TYPE.new_story;
            case best:
                return Story.TYPE.best_story;
            default:
                new DeveloperError("Bad Query type");
                return null;
        }
    }

    private String getOrder(){
        return ItemEntry.COLUMN_RANK + " ASC" +
                ", " + ItemEntry.COLUMN_TIMESTAMP + " ASC";
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyNewsUri = ItemEntry.buildStoriesUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                STORY_COLUMNS,
                ItemEntry.COLUMN_TYPE + " = ?",
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
        storiesAdapter.swapCursor(null);
        stopRefreshing();
    }

}
