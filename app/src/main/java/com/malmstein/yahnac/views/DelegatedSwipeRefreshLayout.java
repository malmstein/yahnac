package com.malmstein.yahnac.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class DelegatedSwipeRefreshLayout extends SwipeRefreshLayout {

    private ViewDelegate delegate;

    public DelegatedSwipeRefreshLayout(Context context) {
        super(context);
    }

    public DelegatedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewDelegate(ViewDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean canChildScrollUp() {
        if (delegate != null) {
            return delegate.isReadyForPull();
        }
        return super.canChildScrollUp();
    }

}
