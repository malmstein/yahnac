package com.malmstein.hnews.stories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.inject.Inject;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.presenters.StoriesCursorAdapter;
import com.malmstein.hnews.views.ViewDelegate;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int position = storiesCursorAdapter.getSelectedPosition();
        if (position != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, position);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected StoriesCursorAdapter getStoriesAdapter(StoryListener listener) {
        return new StoriesCursorAdapter(getActivity(), null, 0, listener);
    }

    private Story.TYPE getType(){
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
        QUERY query = (QUERY) getArguments().get("query");
        switch (query){
            case top:
                return ItemEntry.COLUMN_ITEM_ORDER + " ASC";
            case newest:
                return ItemEntry.COLUMN_TIME + " DESC";
            case best:
                return ItemEntry.COLUMN_SCORE + " DESC";
            default:
                return null;
        }

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
        stopRefreshing();
        storiesCursorAdapter.swapCursor(data);
        int position = storiesCursorAdapter.getSelectedPosition();
        if (position != ListView.INVALID_POSITION) {
            storiesList.smoothScrollToPosition(position);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        stopRefreshing();
        storiesCursorAdapter.swapCursor(null);
        stopRefreshing();
    }

    @Override
    public void onRefreshDelegated() {
        DataRepository dataRepository = Inject.dataRepository();
        dataRepository.getStoriesFrom(getType());
        subscription = dataRepository
                .getStoriesFrom(getType())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Got stuff ?", "Got stuff completed ! ");
                        if (!subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Got stuff ?", "Error !", e);
                    }

                    @Override
                    public void onNext(Integer rowsAffected) {
                        Log.d("Got stuff ?", "Got stuff ! " + rowsAffected);
                        stopRefreshing();
                    }
                });
    }

}
