package com.malmstein.yahnac.views.toolbar;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.malmstein.yahnac.views.quickreturn.AbstractUiShowHideAnimator;
import com.malmstein.yahnac.views.quickreturn.TranslateUiShowHideAnimator;

public class HNToolbar extends Toolbar {

    private boolean showing;
    private TranslateUiShowHideAnimator showHideAnimator;

    public HNToolbar(Context context) {
        super(context);
        init();
    }

    public HNToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HNToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        showHideAnimator = new TranslateUiShowHideAnimator(this, AbstractUiShowHideAnimator.UiElementLocation.TOP);
        showing = true;
    }

    public void show() {
        if (showing || isAnimating()) {
            return;
        }

        setVisibility(VISIBLE);
        showHideAnimator.show();
    }

    public void hide() {
        if (!showing || isAnimating()) {
            return;
        }

        showHideAnimator.hide()
                .setEndAction(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(View.INVISIBLE);
                    }
                });
    }

    public boolean isShowing() {
        return showing;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        showing = (visibility == VISIBLE);
    }

    public boolean isAnimating() {
        return showHideAnimator.isAnimating();
    }

}
