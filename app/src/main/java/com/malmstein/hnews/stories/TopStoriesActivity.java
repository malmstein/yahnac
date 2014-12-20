package com.malmstein.hnews.stories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.sync.HNewsSyncAdapter;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.simple.Log;

public class TopStoriesActivity extends HNewsActivity implements TopStoriesFragment.Listener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private int refreshViewOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setToolbar();
        setupRefreshLayout();

        HNewsSyncAdapter.initializeSyncAdapter(this);
    }

    private void setupRefreshLayout() {
        refreshLayout = Views.findById(this, R.id.feed_refresh);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.dark_orange);
        refreshLayout.setOnRefreshListener(this);
        refreshViewOffset = getResources().getDimensionPixelSize(R.dimen.feed_refresh_top_padding);
        refreshLayout.setProgressViewOffset(false, 0, refreshViewOffset);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        HNewsSyncAdapter.syncImmediately(this);
        updateProgressbar(true);
    }

    private void updateProgressbar(final boolean visible) {
        refreshLayout.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(visible);
            }
        });
        Log.w("Refreshing: " + visible);
    }

    @Override
    public void onShareClicked(Intent shareIntent) {
        startActivity(shareIntent);
    }

    @Override
    public void onCommentsClicked() {

    }

    @Override
    public void onContentClicked(Long internalId) {
        startActivity(new Intent(this, ArticleActivity.class).putExtra(ArticleFragment.ARG_STORY_ID, internalId));
    }

    @Override
    public void onLoadFinished() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onSwipeLayoutUpdated(boolean state) {
        refreshLayout.setEnabled(state);

    }

}
