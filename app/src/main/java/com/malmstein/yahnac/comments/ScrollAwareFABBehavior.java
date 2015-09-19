package com.malmstein.yahnac.comments;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import com.malmstein.yahnac.data.updater.LoginSharedPreferences;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    private LoginSharedPreferences loginSharedPreferences;
    private boolean isAnimatingOut = false;

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
        loginSharedPreferences = LoginSharedPreferences.newInstance();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout,
                                       final FloatingActionButton child,
                                       final View directTargetChild,
                                       final View target, final int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout,
                child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {

        super.onNestedScroll(coordinatorLayout, child, target,
                dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (loginSharedPreferences.isLoggedIn()) {
            if (dyConsumed > 0 && !this.isAnimatingOut && child.getVisibility() == View.VISIBLE) {
                animateOut(child);
            } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
                animateIn(child);
            }
        }

    }

    private void animateOut(final FloatingActionButton button) {
        button.setScaleX(1);
        button.setScaleY(1);
        button.animate()
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimatingOut = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimatingOut = false;
                        button.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    private void animateIn(FloatingActionButton button) {
        button.setVisibility(View.VISIBLE);
        button.setScaleX(0);
        button.setScaleY(0);
        button.animate()
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(null)
                .start();
    }
}
