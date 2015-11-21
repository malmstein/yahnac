package com.malmstein.yahnac.comments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;

import com.malmstein.yahnac.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommentsPresenterTest {

    @Mock
    CommentsActivity activity;

    @Mock
    SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Mock
    MenuItem menuItem;

    @Mock
    CommentsView commentsView;

    CommentsPresenter commentsPresenter;

    @Before
    public void setUp() {
        initMocks(this);

        commentsPresenter = createPresenter();
    }

    @Test
    public void removesBookmarkWhenBookmarkItemWasSelected() {
        when(menuItem.getItemId()).thenReturn(R.id.action_bookmark);
        when(menuItem.isChecked()).thenReturn(true);

        commentsPresenter.onOptionsItemSelected(menuItem);

        verify(menuItem).setIcon(R.drawable.ic_bookmark_outline_white);
        verify(menuItem).setChecked(false);
    }

    @Test
    public void addsBookmarkWhenBookmarkItemWasNotSelected() {
        when(menuItem.getItemId()).thenReturn(R.id.action_bookmark);
        when(menuItem.isChecked()).thenReturn(false);

        commentsPresenter.onOptionsItemSelected(menuItem);

        verify(menuItem).setIcon(R.drawable.ic_bookmark_white);
        verify(menuItem).setChecked(true);
    }

//    @Test
//    public void showsSwipeToRefreshWhenRefreshingAndOnline() {
//        when(activity.findViewById(R.id.comments_view)).thenReturn(commentsView);
//
//        commentsPresenter.onCreate();
//        commentsPresenter.onRefresh(true);
//
//        verify(commentsView).startRefreshing();
//    }
//
//    @Test
//    public void hidesSwipeToRefreshWhenRefreshingAndOffline() {
//        when(activity.findViewById(R.id.comments_view)).thenReturn(commentsView);
//
//        commentsPresenter.onCreate();
//        commentsPresenter.onRefresh(false);
//
//        verify(commentsView).stopRefreshing();
//    }

    private CommentsPresenter createPresenter() {
        return new CommentsPresenter(activity, refreshListener);
    }

}
