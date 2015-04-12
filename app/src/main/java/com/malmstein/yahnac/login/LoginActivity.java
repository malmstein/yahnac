package com.malmstein.yahnac.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;

public class LoginActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupSubActivity();

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(LoginFragment.EXTRA_CX) && getIntent().hasExtra(LoginFragment.EXTRA_CY)) {
                showLoginBox(getIntent().getIntExtra(LoginFragment.EXTRA_CX, 0),
                        getIntent().getIntExtra(LoginFragment.EXTRA_CY, 0));
            }
        }
    }

    private void showLoginBox(int cx, int cy) {
        HNewsFragment fragment = LoginFragment.newInstance(cx, cy);
        getSupportFragmentManager().beginTransaction().add(R.id.login_container, fragment).commit();
    }

    private void hideLoginBox() {
        // previously visible view
        final View myView = findViewById(R.id.login_container);

        // get the center for the clipping circle
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth();

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
