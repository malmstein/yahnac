package com.malmstein.hnews;

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
import android.widget.ListView;

import com.malmstein.hnews.model.Item;
import com.malmstein.hnews.presenters.NewsAdapter;

import static com.malmstein.hnews.data.HNewsContract.ItemEntry;
import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_NEWS_TYPE = BuildConfig.APPLICATION_ID + ".ARG_NEWS_TYPE";
    private static final int STORY_LOADER = 0;

    private ListView mNewsListView;
    private NewsAdapter mNewsAdapter;

    public static NewsListFragment from(Item.TYPE type) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS_TYPE, type);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
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

        return rootView;
    }

    private Item.TYPE getType() {
        Bundle args = getArguments();
        return (Item.TYPE) args.getSerializable(ARG_NEWS_TYPE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri storyNewsUri = ItemEntry.buildItemsUri(getType());

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                STORY_COLUMNS,
                null,
                null,
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

}
