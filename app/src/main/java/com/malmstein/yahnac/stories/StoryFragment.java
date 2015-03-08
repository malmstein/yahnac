package com.malmstein.yahnac.stories;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataRepository;
import com.malmstein.yahnac.data.HNewsContract;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.presenters.ScrollManager;
import com.malmstein.yahnac.presenters.StoriesAdapter;
import com.malmstein.yahnac.views.DelegatedSwipeRefreshLayout;
import com.malmstein.yahnac.views.SnackBarView;
import com.malmstein.yahnac.views.ViewDelegate;
import com.malmstein.yahnac.views.recyclerview.FeedRecyclerItemDecoration;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public abstract class StoryFragment extends HNewsFragment implements SwipeRefreshLayout.OnRefreshListener, ViewDelegate, ScrollManager.Listener {

    protected StoriesAdapter storiesAdapter;
    protected Subscription subscription;
    private RecyclerView storiesList;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private StoryListener listener;
    private DelegatedSwipeRefreshLayout refreshLayout;
    private String nextUrl;

    private SnackBarView snackbarView;
    private int croutonBackgroundAlpha;
    private long croutonAnimationDuration;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (StoryListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stories_feed, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        storiesList = Views.findById(rootView, R.id.list_news);
        snackbarView = Views.findById(rootView, R.id.feed_page_snackbar);

        this.croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        this.croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);

        setupRefreshLayout();
        setupStoriesList();
        maybeUpdateContent();

        return rootView;
    }

    protected void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
    }

    private void setupStoriesList() {
        storiesList.setHasFixedSize(true);
        storiesLayoutManager = new LinearLayoutManager(getActivity());
        storiesList.addItemDecoration(createItemDecoration(getResources()));
        storiesList.setLayoutManager(storiesLayoutManager);
        storiesList.setOnScrollListener(new ScrollManager(this));

        storiesAdapter = new StoriesAdapter(null, listener);
        storiesList.setAdapter(storiesAdapter);
    }

    private void maybeUpdateContent() {
        if (isOnline()) {
            DataRepository dataRepository = Inject.dataRepository();
            if (dataRepository.shouldUpdateContent(getType())) {
                startRefreshing();
                onRefresh();
            }
        }
    }

    private FeedRecyclerItemDecoration createItemDecoration(Resources resources) {
        int verticalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_divider_height);
        int horizontalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_padding_infra_spans);
        return new FeedRecyclerItemDecoration(verticalItemSpacingInPx, horizontalItemSpacingInPx);
    }

    protected abstract Story.TYPE getType();

    protected String getOrder() {
        return HNewsContract.ItemEntry.COLUMN_RANK + " ASC" +
                ", " + HNewsContract.ItemEntry.COLUMN_TIMESTAMP + " ASC";
    }

    protected void startRefreshing() {
        refreshLayout.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    protected void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    protected void disableRefresh() {
        refreshLayout.setEnabled(false);
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(storiesList, -1);
    }

    @Override
    public void onRefresh() {
        subscribeToStories();
    }

    @Override
    public void onLoadMoreItems() {
        showLoadingSnackbar();
        subscribeToStories();
    }

    public void showLoadingSnackbar() {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_text_loading))
                .withBackgroundColor(R.color.orange, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .animating();
    }

    private void subscribeToStories() {
        if (isOnline()) {
            DataRepository dataRepository = Inject.dataRepository();
            subscription = dataRepository
                    .observeStories(getType(), nextUrl)
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
                            Inject.crashAnalytics().logSomethingWentWrong("DataRepository: getStoriesFrom " + getType().toString(), e);
                        }

                        @Override
                        public void onNext(String moreItemsUrl) {
                            nextUrl = moreItemsUrl;
                        }
                    });
        } else {
            // TODO maybe show snackbar?
            stopRefreshing();
        }
    }

}
