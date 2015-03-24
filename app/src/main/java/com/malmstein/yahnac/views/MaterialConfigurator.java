package com.malmstein.yahnac.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

public class MaterialConfigurator {

    private static final int NO_ID = -1;

    private int toolbarId = NO_ID;
    private int viewUnderStatusBarResourceId = NO_ID;
    private int statusBarColorResourceId = NO_ID;
    private int contentLayoutId = NO_ID;

    public MaterialConfigurator() {
    }

    public MaterialConfigurator withContentView(@LayoutRes int contentLayoutId) {
        this.contentLayoutId = contentLayoutId;
        return this;
    }

    public MaterialConfigurator withToolbar(@IdRes int toolbarId) {
        this.toolbarId = toolbarId;
        return this;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialConfigurator withStatusBarOnTopOf(@IdRes int viewUnderStatusBarResourceId) {
        this.viewUnderStatusBarResourceId = viewUnderStatusBarResourceId;
        return this;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialConfigurator withStatusBarColor(@ColorRes int statusBarColorResourceId) {
        this.statusBarColorResourceId = statusBarColorResourceId;
        return this;
    }

    public void materialize(ActionBarActivity activity) {
        if (contentLayoutId != NO_ID) {
            activity.setContentView(contentLayoutId);
        }
        if (toolbarId != NO_ID) {
            setToolbar(activity, toolbarId);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (viewUnderStatusBarResourceId != NO_ID) {
            View view = activity.findViewById(viewUnderStatusBarResourceId);
            setStatusBarOnTopOf(view);
        }
        if (statusBarColorResourceId != NO_ID) {
            int color = activity.getResources().getColor(statusBarColorResourceId);
            setStatusBarColor(activity, color);
        }
    }

    private void setToolbar(ActionBarActivity activity, int toolbarId) {
        View view = activity.findViewById(toolbarId);
        if (view instanceof Toolbar) {
            activity.setSupportActionBar((Toolbar) view);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarOnTopOf(View view) {
        view.setOnApplyWindowInsetsListener(new InsetsListener());
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarColor(Activity activity, int color) {
        activity.getWindow().setStatusBarColor(color);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static class InsetsListener implements View.OnApplyWindowInsetsListener {

        @Override
        public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
            applyAsMarginsOrPaddings(view, insets);
            return insets.consumeSystemWindowInsets();
        }

        public void applyAsMarginsOrPaddings(View view, WindowInsets insets) {
            if (!ViewCompat.getFitsSystemWindows(view)) {
                applyAsMargins(view, insets);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        applyAsPaddings(child, copy(insets));
                    } else {
                        applyAsMargins(child, insets);
                    }
                }
            } else {
                applyAsPaddings(view, insets);
            }
        }

        private static WindowInsets copy(WindowInsets insets) {
            return insets.replaceSystemWindowInsets(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom());
        }

        private void applyAsMargins(View child, WindowInsets insets) {
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;
                marginLayoutParams.leftMargin = insets.getSystemWindowInsetLeft();
                marginLayoutParams.topMargin = insets.getSystemWindowInsetTop();
                marginLayoutParams.rightMargin = insets.getSystemWindowInsetRight();
                marginLayoutParams.bottomMargin = insets.getSystemWindowInsetBottom();
            }
        }

        private void applyAsPaddings(View view, WindowInsets insets) {
            view.dispatchApplyWindowInsets(insets);
        }

    }

}