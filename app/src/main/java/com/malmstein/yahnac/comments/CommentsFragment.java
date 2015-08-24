package com.malmstein.yahnac.comments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.data.Provider;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.presenters.CommentsAdapter;
import com.malmstein.yahnac.presenters.CursorRecyclerAdapter;
import com.malmstein.yahnac.views.DelegatedSwipeRefreshLayout;
import com.malmstein.yahnac.views.OffsetAwareAppBarLayout;
import com.malmstein.yahnac.views.ViewDelegate;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;
import com.novoda.notils.exception.DeveloperError;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class CommentsFragment extends HNewsFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    public static final String TAG = "CommentsFragment";
    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_COMMENT_STORY";
    private static final int COMMENTS_LOADER = 1006;

    private DelegatedSwipeRefreshLayout refreshLayout;
    private RecyclerView commentsList;
    private CursorRecyclerAdapter commentsAdapter;
    private RecyclerView.LayoutManager commentsLayoutManager;

    private CommentsAdapter.Listener listener;

    private Subscription subscription;
    private OffsetAwareAppBarLayout.Status toolbarStatus;

    public static CommentsFragment from(Story story) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, story);
        CommentsFragment fragment = new CommentsFragment();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = Classes.from(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        commentsList = Views.findById(rootView, R.id.list_comments);

        setupRefreshLayout();
        setupCommentsList();
        listenToAppBarChanges();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(COMMENTS_LOADER, null, this);
        getComments();
    }

    private void getComments() {
        if (isOnline()) {
            startRefreshing();
            Provider provider = Inject.provider();
            subscription = provider
                    .observeComments(getStory().getId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
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
                        public void onNext(Integer header) {

                        }
                    });
        }
    }

    private void startRefreshing() {
        refreshLayout.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    private void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
    }

    private void setupCommentsList() {
        commentsList.setHasFixedSize(true);
        commentsLayoutManager = new LinearLayoutManager(getActivity());
        commentsList.setLayoutManager(commentsLayoutManager);

        commentsAdapter = new CommentsAdapter(getStory().getType(), null, listener);
        commentsList.setAdapter(commentsAdapter);
    }

    private void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    private void listenToAppBarChanges() {
        OffsetAwareAppBarLayout appBarLayout = Views.findById(getActivity(), R.id.appbar);
        appBarLayout.setOnStatusChangeListener(new OffsetAwareAppBarLayout.OnStatusChangeListener() {
            @Override
            public void onStatusChanged(OffsetAwareAppBarLayout.Status toolbarChange) {
                toolbarStatus = toolbarChange;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri commentsUri = HNewsContract.CommentsEntry.buildCommentsUri();

        return new CursorLoader(
                getActivity(),
                commentsUri,
                HNewsContract.CommentsEntry.COMMENT_COLUMNS,
                HNewsContract.StoryEntry.ITEM_ID + " = ?",
                new String[]{String.valueOf(getStory().getId())},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        commentsAdapter.swapCursor(data);
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
        return (ViewCompat.canScrollVertically(commentsList, -1) && toolbarStatus.equals(ControllableAppBarLayout.State.EXPANDED));
    }

}
