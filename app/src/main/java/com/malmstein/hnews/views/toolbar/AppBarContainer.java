package com.malmstein.hnews.views.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.malmstein.hnews.R;
import com.malmstein.hnews.views.InsetColorDrawable;
import com.malmstein.hnews.views.quickreturn.TranslateUiShowHideAnimator;
import com.novoda.notils.exception.DeveloperError;

public class AppBarContainer extends LinearLayout implements TranslateUiShowHideAnimator.TranslationAmountProvider {

    private HNToolbar appBar;
    private boolean appBarShowing;
    private TranslateUiShowHideAnimator showHideAnimator;
    private InsetColorDrawable background;
    private int shadowHeightPx;

    public AppBarContainer(Context context) {
        this(context, null);
    }

    public AppBarContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppBarContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFrom(context, attrs);
        setOrientation(VERTICAL);
        showHideAnimator = new FadeAndTranslateUiShowHideAnimator(this);
        appBarShowing = true;
    }

    private void initFrom(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppBarContainer);
        if (a != null) {
            try {
                int appBarColor = a.getColor(R.styleable.AppBarContainer_appBarColor, getResources().getColor(R.color.orange));
                int shadowHeight = a.getDimensionPixelSize(R.styleable.AppBarContainer_appBarShadowHeight, getResources().getDimensionPixelSize(R.dimen.app_bar_shadow_height));
                shadowHeightPx = shadowHeight;
                initBackground(appBarColor, shadowHeight);

                int appBarLayoutId = a.getResourceId(R.styleable.AppBarContainer_appBarLayout, R.layout.include_app_bar);
                initAppBar(appBarLayoutId);
            } finally {
                a.recycle();
            }
        }
    }

    private void initBackground(int appBarColor, int shadowHeight) {
        Rect insets = new Rect(0, 0, 0, shadowHeight);
        this.background = new InsetColorDrawable(appBarColor, insets);
        super.setBackground(background);
    }

    private void initAppBar(int appBarLayoutId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(appBarLayoutId, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addShadowView();
    }

    private void addShadowView() {
        FrameLayout shadowView = new FrameLayout(getContext());
        shadowView.setBackground(getResources().getDrawable(R.drawable.shape_appbar_shadow));
        addView(shadowView, LayoutParams.MATCH_PARENT, shadowHeightPx);
    }

    @Override
    public void setBackground(Drawable background) {
        throw new DeveloperError("AppBarContainer does not allow you to set a background");
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation != VERTICAL) {
            throw new DeveloperError("AppBarContainer supports only vertical orientation");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void setBackgroundColor(int color) {
        background.setColor(color);
    }

    public void setAppBar(HNToolbar appBar) {
        this.appBar = appBar;
    }

    @Override
    public int getHideableHeightPx() {
        return appBar.getHeight() - appBar.getTopInset();
    }

    public void showAppBar() {
        if (appBarShowing || isAppBarAnimating()) {
            return;
        }

        showHideAnimator.show();
        appBarShowing = true;
    }

    public void hideAppBar() {
        if (!appBarShowing || isAppBarAnimating()) {
            return;
        }

        showHideAnimator.hide();
        appBarShowing = false;
    }

    public boolean isAppBarShowing() {
        return appBarShowing;
    }

    public boolean isAppBarAnimating() {
        return showHideAnimator.isAnimating();
    }

    private class FadeAndTranslateUiShowHideAnimator extends TranslateUiShowHideAnimator {

        public FadeAndTranslateUiShowHideAnimator(AppBarContainer appBarContainer) {
            super(appBarContainer, UiElementLocation.TOP, appBarContainer);
        }

        @Override
        protected void onShowAnimationUpdate(float value, View view, UiElementLocation location) {
            super.onShowAnimationUpdate(value, view, location);
            setAppBarChildrenAlpha(value);
        }

        private void setAppBarChildrenAlpha(float value) {
            for (int i = 0; i < appBar.getChildCount(); i++) {
                appBar.getChildAt(i).setAlpha(value);
            }
        }

        @Override
        protected void onHideAnimationUpdate(float value, View view, UiElementLocation location) {
            super.onHideAnimationUpdate(value, view, location);
            setAppBarChildrenAlpha(1f - value);
        }

    }

}
