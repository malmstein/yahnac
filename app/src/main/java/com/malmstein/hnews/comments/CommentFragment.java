package com.malmstein.hnews.comments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.inject.Inject;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.presenters.CommentsAdapter;
import com.malmstein.hnews.views.DelegatedSwipeRefreshLayout;
import com.malmstein.hnews.views.ViewDelegate;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.malmstein.hnews.data.HNewsContract.COMMENT_COLUMNS;
import static com.malmstein.hnews.data.HNewsContract.ItemEntry;

public class CommentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    public static final String TAG = "CommentsFragment";
    private static final int COMMENTS_LOADER = 0;

    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_COMMENT_STORY";

    private DelegatedSwipeRefreshLayout refreshLayout;
    private RecyclerView commentsListView;
    private CommentsAdapter commentsAdapter;
    private RecyclerView.LayoutManager commentsLayoutManager;

    private TextView text;
    private TextView author;
    private TextView when;

    private Subscription subscription;

    public static CommentFragment from(Story story) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, story);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Story getStory() {
        if (getArguments().containsKey(ARG_STORY)) {
            return (Story) getArguments().getSerializable(ARG_STORY);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    private String getTitle() {
        return getResources().getQuantityString(R.plurals.story_comments,
                getStory().getComments(),
                getStory().getComments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        commentsListView = Views.findById(rootView, R.id.list_comments);

        text = Views.findById(rootView, R.id.comment_text);
        author = Views.findById(rootView, R.id.comment_by);
        when = Views.findById(rootView, R.id.comment_when);

        setupHeader();
        setupRefreshLayout();
        setupCommentsList();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COMMENTS_LOADER, null, this);
        startRefreshing();
        getComments();
    }

    private void getComments() {
        DataRepository dataRepository = Inject.dataRepository();
        subscription = dataRepository
                .getCommentsFromStory(getStory().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        if (!subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Inject.crashAnalytics().logSomethingWentWrong("DataRepository: getCommentsFrom " + getStory().getId(), e);
                    }

                    @Override
                    public void onNext(String comments) {
                        String header = comments;
                        text.setText(header);
                    }
                });
    }

    private void startRefreshing() {
        refreshLayout.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    private void setupHeader(){
        author.setText(getStory().getSubmitter());
        when.setText(getStory().getTimeAgo());
    }

    private void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
    }

    private void setupCommentsList() {
        commentsListView.setHasFixedSize(true);
        commentsLayoutManager = new LinearLayoutManager(getActivity());
        commentsListView.setLayoutManager(commentsLayoutManager);

        commentsAdapter = new CommentsAdapter(null);
        commentsListView.setAdapter(commentsAdapter);
    }

    private void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri commentsUri = ItemEntry.buildCommentsUri();

        return new CursorLoader(
                getActivity(),
                commentsUri,
                COMMENT_COLUMNS,
                ItemEntry.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(getStory().getId())},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        commentsAdapter.swapCursor(data);
        getActivity().setTitle(getTitle());
        stopRefreshing();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        commentsAdapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        getComments();
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(commentsListView, -1);
    }

}
