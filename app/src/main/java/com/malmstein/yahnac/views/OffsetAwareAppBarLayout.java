package com.malmstein.yahnac.views;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;

public class OffsetAwareAppBarLayout extends AppBarLayout implements AppBarLayout.OnOffsetChangedListener {

    private OnStatusChangeListener onStatusChangeListener;
    private Status status;

    public OffsetAwareAppBarLayout(Context context) {
        super(context);
    }

    public OffsetAwareAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (onStatusChangeListener != null && status != Status.EXPANDED) {
                onStatusChangeListener.onStatusChanged(Status.EXPANDED);
            }
            status = Status.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (onStatusChangeListener != null && status != Status.COLLAPSED) {
                onStatusChangeListener.onStatusChanged(Status.COLLAPSED);
            }
            status = Status.COLLAPSED;
        } else {
            if (onStatusChangeListener != null && status != Status.IDLE) {
                onStatusChangeListener.onStatusChanged(Status.IDLE);
            }
            status = Status.IDLE;
        }
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.onStatusChangeListener = listener;
    }

    public enum Status {
        COLLAPSED,
        EXPANDED,
        IDLE
    }

    public interface OnStatusChangeListener {
        void onStatusChanged(Status toolbarChange);
    }
}
