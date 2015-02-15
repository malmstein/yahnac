package com.malmstein.hnews.stories;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.hnews.R;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.inject.Inject;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.presenters.StoriesAdapter;
import com.malmstein.hnews.views.DelegatedSwipeRefreshLayout;
import com.malmstein.hnews.views.ViewDelegate;
import com.malmstein.hnews.views.recyclerview.FeedRecyclerItemDecoration;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public abstract class StoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    private RecyclerView storiesList;
    protected StoriesAdapter storiesAdapter;
    private RecyclerView.LayoutManager storiesLayoutManager;

    private StoryListener listener;
    private DelegatedSwipeRefreshLayout refreshLayout;

    protected Subscription subscription;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (StoryListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentLayoutId(), container, false);

        refreshLayout = Views.findById(rootView, R.id.feed_refresh);
        storiesList = Views.findById(rootView, R.id.list_news);

        setupRefreshLayout();
        setupStoriesList();
        maybeUpdateContent();

        return rootView;
    }

    protected abstract int getFragmentLayoutId();

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

        storiesAdapter = new StoriesAdapter(null, listener);
        storiesList.setAdapter(storiesAdapter);
    }

    private void maybeUpdateContent() {
        DataRepository dataRepository = Inject.dataRepository();
        if (dataRepository.shouldUpdateContent()) {
            onRefresh();
        }
    }

    private FeedRecyclerItemDecoration createItemDecoration(Resources resources) {
        int verticalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_divider_height);
        int horizontalItemSpacingInPx = resources.getDimensionPixelSize(R.dimen.feed_padding_infra_spans);
        return new FeedRecyclerItemDecoration(verticalItemSpacingInPx, horizontalItemSpacingInPx);
    }

    @Override
    public void onRefresh() {
        maybeShowRefreshing();
        DataRepository dataRepository = Inject.dataRepository();
        subscription = dataRepository
                .getStoriesFrom(getType())
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
                    public void onNext(Integer rowsAffected) {
                        stopRefreshing();
                    }
                });
    }

    protected abstract Story.TYPE getType();

    protected void maybeShowRefreshing(){
        if (!refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }

    protected void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(storiesList, -1);
    }

}
