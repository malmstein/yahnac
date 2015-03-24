package com.malmstein.yahnac.stories;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Toast;

import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.base.TimeAgo;
import com.malmstein.yahnac.data.DataRepository;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.presenters.StoriesAdapter;
import com.malmstein.yahnac.views.DelegatedSwipeRefreshLayout;
import com.malmstein.yahnac.views.ViewDelegate;
import com.malmstein.yahnac.views.quickreturn.RecyclerViewQuickReturnHinter;
import com.malmstein.yahnac.views.recyclerview.FeedRecyclerItemDecoration;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public abstract class StoryFragment extends HNewsFragment implements SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    protected StoriesAdapter storiesAdapter;
    protected Subscription subscription;

    private RecyclerView storiesList;
    private RecyclerView.LayoutManager storiesLayoutManager;
    private DelegatedSwipeRefreshLayout refreshLayout;
    private int refreshViewOffset;

    private RecyclerViewQuickReturnHinter quickReturnHinter;
    private StoryListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (StoryListener) getActivity();
        quickReturnHinter = createQuickReturnHinter(listener);
    }

    private RecyclerViewQuickReturnHinter createQuickReturnHinter(StoryListener listener) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getActivity());
        int longScrollSlop = viewConfiguration.getScaledTouchSlop();
        int shortScrollSlop = longScrollSlop / 2;
        return new RecyclerViewQuickReturnHinter(longScrollSlop, shortScrollSlop, listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stories_feed, container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        storiesList = Views.findById(rootView, R.id.list_news);

        setupRefreshLayout();
        setupStoriesList();
        maybeUpdateContent();

        return rootView;
    }

    protected void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
        refreshViewOffset = getResources().getDimensionPixelSize(R.dimen.feed_refresh_top_padding);
        refreshLayout.setProgressViewOffset(false, 0, refreshViewOffset);
    }

    private void setupStoriesList() {
        storiesList.setHasFixedSize(true);
        storiesLayoutManager = createLayoutManager(getResources());
        storiesList.addItemDecoration(createItemDecoration(getResources()));
        storiesList.setLayoutManager(storiesLayoutManager);

        storiesAdapter = new StoriesAdapter(null, listener, new TimeAgo(getActivity().getResources()));
        storiesList.setAdapter(storiesAdapter);

        updateOnScrollListener();
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

    private void updateOnScrollListener() {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                quickReturnHinter.onScrolled(recyclerView, dx, dy);
            }
        };
        storiesList.setOnScrollListener(onScrollListener);
    }

    private FeedRecyclerItemDecoration createItemDecoration(Resources resources) {
        int verticalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_divider_height);
        int horizontalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_padding_infra_spans);
        return new FeedRecyclerItemDecoration(verticalItemSpacingInPx, horizontalItemSpacingInPx);
    }

    private RecyclerView.LayoutManager createLayoutManager(Resources resources) {
        int spans = resources.getInteger(R.integer.feed_columns);
        return new StaggeredGridLayoutManager(spans, RecyclerView.VERTICAL);
    }

    protected abstract Story.TYPE getType();

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

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(storiesList, -1);
    }

    @Override
    public void onRefresh() {
        subscribeToStories();
    }

    private void subscribeToStories() {
        if (isOnline()) {
            DataRepository dataRepository = Inject.dataRepository();
            subscription = dataRepository
                    .getStories(getType())
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
                            Inject.crashAnalytics().logSomethingWentWrong("DataRepository: getStoriesFrom " + getType().toString(), e);
                        }

                        @Override
                        public void onNext(Integer total) {
                            Toast.makeText(getActivity(), "total items:" + total, Toast.LENGTH_LONG);
                        }
                    });
        } else {
            stopRefreshing();
        }
    }

    public void updateProgressViewOffset(int topInset) {
        refreshLayout.setProgressViewOffset(false, 0, topInset + refreshViewOffset);
    }
}
