package com.malmstein.hnews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.malmstein.hnews.R;

public class RelativeLayoutWithForeground extends RelativeLayout {

    private static final int INVALID_FOREGROUND_ID = 0;

    private Drawable foreground;

    public RelativeLayoutWithForeground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RelativeLayoutWithForeground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LayoutWithForeground);
        if (a == null) {
            return;
        }
        try {
            int resourceId = a.getResourceId(R.styleable.LayoutWithForeground_foreground, INVALID_FOREGROUND_ID);
            if (resourceId == INVALID_FOREGROUND_ID) {
                return;
            }
            Drawable drawable = getResources().getDrawable(resourceId);
            updateForeground(drawable);
        } finally {
            a.recycle();
        }
    }

    public Drawable getForeground() {
        return foreground;
    }

    public void setForeground(Drawable foreground) {
        if (this.foreground == foreground) {
            return;
        }
        removeOldForegroundIfAny();
        updateForeground(foreground);
    }

    private void updateForeground(Drawable foreground) {
        this.foreground = foreground;
        if (foreground != null) {
            setWillNotDraw(false);
            foreground.setCallback(this);
            if (foreground.isStateful()) {
                foreground.setState(getDrawableState());
            }
        } else {
            setWillNotDraw(true);
        }
        requestLayout();
        invalidate();
    }

    private void removeOldForegroundIfAny() {
        if (foreground != null) {
            foreground.setCallback(null);
            unscheduleDrawable(foreground);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || (who == foreground);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (foreground != null) {
            foreground.jumpToCurrentState();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (foreground != null && foreground.isStateful()) {
            foreground.setState(getDrawableState());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (foreground != null) {
            foreground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (foreground != null) {
            foreground.draw(canvas);
        }
    }

}
