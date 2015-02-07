package com.malmstein.hnews.stories;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.data.DataRepository;
import com.malmstein.hnews.inject.Inject;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.presenters.StoriesCursorAdapter;
import com.malmstein.hnews.views.DelegatedSwipeRefreshLayout;
import com.malmstein.hnews.views.ViewDelegate;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public abstract class StoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    protected static final String SELECTED_KEY = "selected_position";

    protected ListView storiesList;
    protected StoriesCursorAdapter storiesCursorAdapter;

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
        storiesList = (ListView) rootView.findViewById(R.id.listview_news);
        storiesList.setEmptyView(rootView.findViewById(R.id.feed_empty_placeholder));
        setupRefreshLayout();
        setupStoriesList();

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            storiesCursorAdapter.setSelectedPosition(savedInstanceState.getInt(SELECTED_KEY));
        }

        return rootView;
    }

    protected abstract int getFragmentLayoutId();

    protected void setupRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setViewDelegate(this);
    }

    private void setupStoriesList() {
        storiesCursorAdapter = getStoriesAdapter(listener);
        storiesList.setAdapter(storiesCursorAdapter);
    }

    protected abstract StoriesCursorAdapter getStoriesAdapter(StoryListener listener);

    @Override
    public void onRefresh() {
        maybeShowRefreshing();
        DataRepository dataRepository = Inject.dataRepository();
        dataRepository.getStoriesFrom(getType());
        subscription = dataRepository
                .getStoriesFrom(getType())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Got stuff ?", "Got stuff completed ! ");
                        if (!subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Got stuff ?", "Error !", e);
                    }

                    @Override
                    public void onNext(Integer rowsAffected) {
                        Log.d("Got stuff ?", "Got stuff ! " + rowsAffected);
                        stopRefreshing();
                    }
                });
    }

    protected abstract Story.TYPE getType();

    protected void maybeShowRefreshing(){
        if (!refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(true);
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
