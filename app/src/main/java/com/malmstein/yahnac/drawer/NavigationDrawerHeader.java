package com.malmstein.yahnac.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.updater.LoginSharedPreferences;

public class NavigationDrawerHeader extends LinearLayout {

    private LoginSharedPreferences loginSharedPreferences;

    public NavigationDrawerHeader(Context context) {
        super(context);
    }

    public NavigationDrawerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationDrawerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        loginSharedPreferences = LoginSharedPreferences.newInstance();
        if (loginSharedPreferences.isLoggedIn()) {
            LayoutInflater.from(getContext()).inflate(R.layout.view_story_comment_header, this, true);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.view_story_comment_header, this, true);
        }

    }

}
