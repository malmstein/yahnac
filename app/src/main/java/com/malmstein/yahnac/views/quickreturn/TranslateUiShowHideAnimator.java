package com.malmstein.yahnac.views.quickreturn;

import android.view.View;
import android.view.ViewGroup;

public class TranslateUiShowHideAnimator extends AbstractUiShowHideAnimator {

    private final TranslationAmountProvider translationAmountProvider;

    private float startTranslationY;
    private float targetTranslationY;

    public TranslateUiShowHideAnimator(View targetView, UiElementLocation viewLocation) {
        this(targetView, viewLocation, null);
    }

    public TranslateUiShowHideAnimator(View targetView, UiElementLocation viewLocation, TranslationAmountProvider translationAmountProvider) {
        super(targetView, viewLocation);
        this.translationAmountProvider = translationAmountProvider;
    }

    @Override
    protected void onPrepareShowAnimation(View view, UiElementLocation location) {
        startTranslationY = getTranslationY(translationAmountProvider, view, location);
        targetTranslationY = 0;
        view.setTranslationY(startTranslationY);
    }

    @Override
    protected void onShowAnimationUpdate(float value, View view, UiElementLocation location) {
        float deltaTranslationY = targetTranslationY - startTranslationY;
        view.setTranslationY(startTranslationY + value * deltaTranslationY);
    }

    @Override
    protected void onPrepareHideAnimation(View view, UiElementLocation location) {
        startTranslationY = 0;
        targetTranslationY = getTranslationY(translationAmountProvider, view, location);
        view.setTranslationY(startTranslationY);
    }

    private int getTranslationY(TranslationAmountProvider translationAmountProvider, View view, UiElementLocation location) {
        if (translationAmountProvider != null) {
            return getTranslationYFrom(translationAmountProvider, location);
        }
        return calculateViewTotalTranslationYForOffscreen(view, location);
    }

    private int getTranslationYFrom(TranslationAmountProvider translationAmountProvider, UiElementLocation location) {
        int translationY = translationAmountProvider.getHideableHeightPx();
        if (location == UiElementLocation.TOP) {
            translationY = -translationY;
        }
        return translationY;
    }

    private int calculateViewTotalTranslationYForOffscreen(View view, UiElementLocation location) {
        int totalY = view.getHeight();
        if (location == UiElementLocation.TOP) {
            totalY += getMarginTopOf(view);
            totalY = -totalY;
        } else {
            totalY += getMarginBottomOf(view);
        }
        return totalY;
    }

    private int getMarginTopOf(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) lp).topMargin;
        }
        return 0;
    }

    private int getMarginBottomOf(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) lp).bottomMargin;
        }
        return 0;
    }

    @Override
    protected void onHideAnimationUpdate(float value, View view, UiElementLocation location) {
        view.setTranslationY(value * targetTranslationY);
    }

    public interface TranslationAmountProvider {

        int getHideableHeightPx();

    }

}
