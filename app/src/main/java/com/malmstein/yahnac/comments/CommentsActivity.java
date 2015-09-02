package com.malmstein.yahnac.comments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;

public class CommentsActivity extends HNewsActivity implements SwipeRefreshLayout.OnRefreshListener {

    private CommentsPresenter commentsPresenter;
    private CommentsOperator commentsOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsPresenter = new CommentsPresenter(this);
        commentsPresenter.onCreate();

        commentsOperator = new CommentsOperator(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null) {
            commentsPresenter.onPostCreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentsOperator.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        commentsPresenter.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_article:
                commentsOperator.onArticleSelected();
                return true;
            case R.id.action_bookmark:
                if (item.isChecked()) {
                    commentsOperator.onBookmarkUnselected();
                    commentsPresenter.onBookmarkUnselected(item);
                } else {
                    commentsOperator.onBookmarkSelected();
                    commentsPresenter.onBookmarkSelected(item);
                }
                return true;
            case R.id.action_share:
                commentsOperator.onShareArticle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        if (isOnline()) {
            commentsOperator.retrieveComments();
            commentsPresenter.showContentUpdating();
        } else {
            commentsPresenter.hideRefreshAnimation();
        }
    }
}
