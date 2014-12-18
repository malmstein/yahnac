package com.malmstein.hnews.stories;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.data.HNewsContract;
import com.malmstein.hnews.model.Item;
import com.malmstein.hnews.presenters.NewsAdapter;

import static com.malmstein.hnews.data.HNewsContract.ItemEntry;
import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class TopStoriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STORY_LOADER = 0;

    private ListView mNewsListView;
    private NewsAdapter mNewsAdapter;
    private Listener listener;

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
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        mNewsListView = (ListView) rootView.findViewById(R.id.listview_news);
        mNewsAdapter = new NewsAdapter(getActivity(), null, 0);
        mNewsListView.setAdapter(mNewsAdapter);
        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mNewsAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    listener.onArticleSelected(cursor.getLong(HNewsContract.COLUMN_ID));
                }
            }
        });

        return rootView;
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
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mNewsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsAdapter.swapCursor(null);
    }

    public interface Listener {
        void onArticleSelected(Long itemId);
    }

}
