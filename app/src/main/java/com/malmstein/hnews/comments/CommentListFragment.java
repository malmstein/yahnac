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

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.R;
import com.malmstein.hnews.presenters.NewsCommentsAdapter;

import static com.malmstein.hnews.data.HNewsContract.COMMENT_COLUMNS;
import static com.malmstein.hnews.data.HNewsContract.ItemEntry;

public class CommentListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COMMENTS_LOADER = 0;
    public static final String ARG_STORY_ID = BuildConfig.APPLICATION_ID + ".ARG_STORY_ID";

    private ListView commentsListView;
    private NewsCommentsAdapter commentsAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COMMENTS_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        commentsListView = (ListView) rootView.findViewById(R.id.listview_comments);
        commentsAdapter = new NewsCommentsAdapter(getActivity(), null, 0);
        commentsListView.setAdapter(commentsAdapter);

        return rootView;
    }

    private String getArgStoryId(){
        return String.valueOf(getActivity().getIntent().getLongExtra(ARG_STORY_ID, 0));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri commentsUri = ItemEntry.buildCommentsUri();

        return new CursorLoader(
                getActivity(),
                commentsUri,
                COMMENT_COLUMNS,
                ItemEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{getArgStoryId()},
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
