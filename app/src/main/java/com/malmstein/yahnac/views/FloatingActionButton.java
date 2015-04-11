package com.malmstein.yahnac.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.malmstein.yahnac.R;

public class FloatingActionButton extends ImageButton {

    private static final int ANIMATION_START_DELAY = 300;
    private static final int ANIMATION_DURATION = 250;
    private static final float TENSION = 1.f;

    private int translateY;

    public FloatingActionButton(Context context) {
        super(context);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        translateY = 2 * getResources().getDimensionPixelOffset(R.dimen.fab_min_size);
    }

    public void hideAnimated() {
        runAnimation(translateY);
    }

    public void showAnimated() {
        runAnimation(0);
    }

    private void runAnimation(int translateY) {
        this.animate()
                .translationY(translateY)
                .setInterpolator(new OvershootInterpolator(TENSION))
                .setDuration(ANIMATION_DURATION)
                .start();
    }

    private void runAnimationDelayed(int translateY) {
        this.animate()
                .translationY(translateY)
                .setInterpolator(new OvershootInterpolator(TENSION))
                .setDuration(ANIMATION_DURATION)
                .setStartDelay(ANIMATION_START_DELAY)
                .start();
    }
}
