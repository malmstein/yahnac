package com.malmstein.yahnac.views.quickreturn;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class AbstractUiShowHideAnimator implements Animator.AnimatorListener {

    public enum UiElementLocation {
        TOP,
        BOTTOM
    }

    protected static final float ANIMATED_VALUE_MIN = 0f;
    protected static final float ANIMATED_VALUE_MAX = 1f;

    protected static final int SHOW_HIDE_ANIMATION_DURATION_MS = 250;  // Same hardcoded in ActionBarImpl#doShow/doHide
    protected static final float INTERPOLATOR_FACTOR = 1.5f;           // Same hardcoded in ActionBarImpl#doShow/doHide

    private final Interpolator showInterpolator;
    private final Interpolator hideInterpolator;

    private final View view;
    private final UiElementLocation location;

    private ValueAnimator hideAnimator;
    private ValueAnimator showAnimator;

    private Runnable endActionRunnable;
    private boolean animating;

    public AbstractUiShowHideAnimator(View targetView, UiElementLocation viewLocation) {
        showInterpolator = new DecelerateInterpolator(INTERPOLATOR_FACTOR);
        hideInterpolator = new AccelerateInterpolator(INTERPOLATOR_FACTOR);
        view = targetView;
        location = viewLocation;
    }

    public final AbstractUiShowHideAnimator show() {
        cancelAnimations();

        onPrepareShowAnimation(view, location);

        if (showAnimator == null) {
            showAnimator = createShowAnimator();
        }
        showAnimator.start();
        return this;
    }

    protected abstract void onPrepareShowAnimation(View view, UiElementLocation location);

    private ValueAnimator createShowAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(ANIMATED_VALUE_MIN, ANIMATED_VALUE_MAX);
        animator.setDuration(SHOW_HIDE_ANIMATION_DURATION_MS);
        animator.setInterpolator(showInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onShowAnimationUpdateInternal(animation);
            }
        });
        animator.addListener(this);
        return animator;
    }

    private void onShowAnimationUpdateInternal(ValueAnimator animation) {
        float animatedValue = (Float) animation.getAnimatedValue();
        onShowAnimationUpdate(animatedValue, view, location);
    }

    protected abstract void onShowAnimationUpdate(float value, View view, UiElementLocation location);

    public final AbstractUiShowHideAnimator hide() {
        cancelAnimations();

        onPrepareHideAnimation(view, location);

        if (hideAnimator == null) {
            hideAnimator = createHideAnimator();
        }
        hideAnimator.start();
        return this;
    }

    protected abstract void onPrepareHideAnimation(View view, UiElementLocation location);

    private ValueAnimator createHideAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(ANIMATED_VALUE_MIN, ANIMATED_VALUE_MAX);
        animator.setDuration(SHOW_HIDE_ANIMATION_DURATION_MS);
        animator.setInterpolator(hideInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onHideAnimationUpdateInternal(animation);
            }
        });
        animator.addListener(this);
        return animator;
    }

    private void onHideAnimationUpdateInternal(ValueAnimator animation) {
        float animatedValue = (Float) animation.getAnimatedValue();
        onHideAnimationUpdate(animatedValue, view, location);
    }

    protected abstract void onHideAnimationUpdate(float value, View view, UiElementLocation location);

    protected final void cancelAnimations() {
        if (hideAnimator != null) {
            hideAnimator.cancel();
        }
        if (showAnimator != null) {
            showAnimator.cancel();
        }
    }

    public void setEndAction(Runnable runnable) {
        endActionRunnable = runnable;
    }

    public boolean isAnimating() {
        return animating;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        animating = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        animating = false;
        if (endActionRunnable != null) {
            endActionRunnable.run();
            endActionRunnable = null;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        animating = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        // No-op
    }

}
