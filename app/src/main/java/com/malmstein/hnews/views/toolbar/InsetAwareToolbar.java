package com.malmstein.hnews.views.toolbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.WindowInsets;

public class InsetAwareToolbar extends Toolbar {

    private int topInset;
    private Listener listener;

    public InsetAwareToolbar(Context context) {
        super(context);
    }

    public InsetAwareToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InsetAwareToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public int getTopInset() {
        return topInset;
    }

    protected void setTopInset(int topInset) {
        int currentTopInset = this.topInset;
        int realPaddingTop = getPaddingTop() - currentTopInset;
        super.setPadding(getPaddingLeft(), realPaddingTop + topInset, getPaddingRight(), getPaddingBottom());
        this.topInset = topInset;
        if (listener != null) {
            listener.onTopInsetChanged(topInset);
        }
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTopInset(insets.top);
            return false;
        }
        // If we're on Lollipop or later, we get insets in onApplyWindowInsets
        return super.fitSystemWindows(insets);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (insets.hasSystemWindowInsets()) {
            Rect systemWindowInsets = getSystemWindowInsetsRectFrom(insets);
            setTopInset(systemWindowInsets.top);
            return insets;
        }
        return super.onApplyWindowInsets(insets);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Rect getSystemWindowInsetsRectFrom(WindowInsets insets) {
        return new Rect(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        topInset = 0;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        topInset = 0;
    }

    public interface Listener {

        void onTopInsetChanged(int topInset);

    }

}
