package com.malmstein.yahnac.login;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.novoda.notils.caster.Views;

public class LoginActivity extends HNewsActivity {

    public static final String VIEW_TOOLBAR_TITLE = "login:toolbar:title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setupSubActivity();

        View toolbarTitle = Views.findById(this, R.id.dialog_login_title);
        ViewCompat.setTransitionName(toolbarTitle, VIEW_TOOLBAR_TITLE);
    }
}
