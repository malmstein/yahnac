package com.malmstein.yahnac.comments;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.base.DeveloperError;
import com.malmstein.yahnac.data.DataRepository;
import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.presenters.CommentsAdapter;
import com.malmstein.yahnac.views.DelegatedSwipeRefreshLayout;
import com.malmstein.yahnac.views.ViewDelegate;
import com.malmstein.yahnac.views.recyclerview.CommentRecyclerItemDecoration;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class CommentsFragment extends HNewsFragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    public static final String TAG = "CommentsFragment";
    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_COMMENT_STORY";
    private static final int COMMENTS_LOADER = 0;
    private ShareActionProvider mShareActionProvider;

    private DelegatedSwipeRefreshLayout refreshLayout;
    private RecyclerView commentsList;
    private CommentsAdapter commentsAdapter;
    private RecyclerView.LayoutManager commentsLayoutManager;

    private Subscription subscription;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        commentsList = Views.findById(rootView, R.id.list_comments);

        setupRefreshLayout();
        setupCommentsList();

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_comments, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareArticleIntent());
        }
        if (getStory().isHackerNewsLocalItem()) {
            MenuItem comments = menu.findItem(R.id.action_article);
            comments.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_article) {
            navigate().toInnerBrowser(getStory());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareArticleIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getStory().getCommentsUrl());
        return shareIntent;
    }

    private void getComments() {
        if (isOnline()){
            startRefreshing();
            DataRepository dataRepository = Inject.dataRepository();
            subscription = dataRepository
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
        commentsList.addItemDecoration(createItemDecoration(getResources()));
        commentsList.setLayoutManager(commentsLayoutManager);

        commentsAdapter = new CommentsAdapter(null);
        commentsList.setAdapter(commentsAdapter);
    }

    private CommentRecyclerItemDecoration createItemDecoration(Resources resources) {
        int verticalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.comments_divider_height);
        int horizontalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.comments_padding_infra_spans);
        Drawable divider = resources.getDrawable(R.drawable.divider_horizontal_bright);
        return new CommentRecyclerItemDecoration(divider, verticalItemSpacingInPx, horizontalItemSpacingInPx);
    }

    private void stopRefreshing() {
        refreshLayout.setRefreshing(false);
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
        getActivity().setTitle(getStoryTitle(data.getCount()));
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
        return ViewCompat.canScrollVertically(commentsList, -1);
    }

    private String getStoryTitle(int comments) {
        return getResources().getQuantityString(R.plurals.story_comments,
                comments,
                comments);
    }
}
