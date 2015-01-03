package com.malmstein.hnews.stories;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.malmstein.hnews.R;
import com.malmstein.hnews.presenters.StoriesCursorAdapter;
import com.malmstein.hnews.views.DelegatedSwipeRefreshLayout;
import com.malmstein.hnews.views.ViewDelegate;
import com.novoda.notils.caster.Views;

public abstract class StoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewDelegate {

    protected static final String SELECTED_KEY = "selected_position";

    protected ListView storiesList;
    protected StoriesCursorAdapter storiesCursorAdapter;

    private StoryListener listener;
    private DelegatedSwipeRefreshLayout refreshLayout;

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
        onRefreshDelegated();
    }

    protected abstract void onRefreshDelegated();

    protected void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isReadyForPull() {
        return ViewCompat.canScrollVertically(storiesList, -1);
    }

}
