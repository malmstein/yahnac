package com.malmstein.yahnac.login;

import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.views.AnimationFactory;
import com.novoda.notils.caster.Views;

public class LoginFragment extends HNewsFragment {

    public static final String EXTRA_CX = BuildConfig.APPLICATION_ID + "EXTRA_CX";
    public static final String EXTRA_CY = BuildConfig.APPLICATION_ID + "EXTRA_CY";

    public LoginFragment() {
    }

    public static LoginFragment newInstance(int centerX, int centerY) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_CX, centerX);
        args.putInt(EXTRA_CY, centerY);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);

                int cx = getArguments().getInt(EXTRA_CX);
                int cy = getArguments().getInt(EXTRA_CY);
                int radius = (int) Math.hypot(right, bottom);

                Animator reveal = AnimationFactory.createRevealAnimation(v, cx, cy, radius);
                reveal.start();
            }
        });

        TextView login = Views.findById(rootView, R.id.login_action);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

}