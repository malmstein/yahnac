package com.malmstein.yahnac.views;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;

public class AnimationFactory {

    public static Animator createRevealAnimation(View v, int cx, int cy, int radius){
        Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
        reveal.setInterpolator(new DecelerateInterpolator(2f));
        reveal.setDuration(1000);
        return reveal;
    }
}
