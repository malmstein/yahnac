package com.malmstein.yahnac.login;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.Provider;
import com.malmstein.yahnac.injection.Inject;
import com.malmstein.yahnac.model.Login;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class LoginActivity extends HNewsActivity implements LoginView.Listener {

    public static final String VIEW_TOOLBAR_TITLE = "login:toolbar:title";

    private TextView headerText;
    private View progressBar;
    private LoginView loginView;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setupSubActivity();

        View appBar = Views.findById(this, R.id.appbar);
        ViewCompat.setTransitionName(appBar, VIEW_TOOLBAR_TITLE);

        headerText = (TextView) findViewById(R.id.login_header_text);
        progressBar = findViewById(R.id.login_progress);
        loginView = Views.findById(this, R.id.login_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Inject.usageAnalytics().trackPage(getString(R.string.analytics_page_login));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loginView.bind(this);
    }

    @Override
    public void onSignIn(final String username, String password) {
        showProgress();
        Provider provider = Inject.provider();
        subscription = provider
                .observeLogin(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login.Status>() {
                    @Override
                    public void onCompleted() {
                        if (!subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Inject.crashAnalytics().logSomethingWentWrong("Provider: login: " + username, e);
                    }

                    @Override
                    public void onNext(Login.Status status) {
                        if (status == Login.Status.SUCCESSFUL) {
                            Inject.usageAnalytics().trackLogin(username);
                            showSuccess(username);
                            navigate().toNews();
                        } else {
                            hideProgress();
                            loginView.showError();
                        }
                    }
                });
    }

    @Override
    public void onCancel() {
        Inject.usageAnalytics().trackEvent(getString(R.string.analytics_event_cancel_login));
        onBackPressed();
    }

    private void showSuccess(String username) {
        String message = String.format(getResources().getString(R.string.navigation_drawer_welcome), username);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showProgress() {
        headerText.setText(R.string.title_checking_account);
        progressBar.setVisibility(View.VISIBLE);
        loginView.showProgress();
    }

    private void hideProgress() {
        headerText.setText(R.string.title_add_account);
        progressBar.setVisibility(View.GONE);
        loginView.hideProgress();
    }
}
