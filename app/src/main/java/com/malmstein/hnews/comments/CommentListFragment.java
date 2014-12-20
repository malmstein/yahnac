package com.malmstein.hnews.comments;

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

import com.malmstein.hnews.R;
import com.malmstein.hnews.presenters.NewsCommentsAdapter;

import static com.malmstein.hnews.data.HNewsContract.COMMENT_COLUMNS;
import static com.malmstein.hnews.data.HNewsContract.ItemEntry;

public class CommentListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STORY_LOADER = 0;

    private ListView commentsListView;
    private NewsCommentsAdapter commentsAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(STORY_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_stories, container, false);

        commentsListView = (ListView) rootView.findViewById(R.id.listview_news);
        commentsAdapter = new NewsCommentsAdapter(getActivity(), null, 0);
        commentsListView.setAdapter(commentsAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri storyNewsUri = ItemEntry.buildItemsUri();

        return new CursorLoader(
                getActivity(),
                storyNewsUri,
                COMMENT_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        commentsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        commentsAdapter.swapCursor(null);
    }
}
