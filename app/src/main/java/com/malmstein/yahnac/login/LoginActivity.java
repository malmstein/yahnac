package com.malmstein.yahnac.login;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.Provider;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Login;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class LoginActivity extends HNewsActivity {

    public static final String VIEW_TOOLBAR_TITLE = "login:toolbar:title";

    private EditText usernameView;
    private EditText passwordView;
    private View errorView;
    private TextView titleView;
    private ProgressBar progressView;
    private View loginView;
    private View cancelView;

    private InputFieldValidator inputFieldValidator = new InputFieldValidator();
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setupSubActivity();

        View toolbarTitle = Views.findById(this, R.id.appbar);
        ViewCompat.setTransitionName(toolbarTitle, VIEW_TOOLBAR_TITLE);

        titleView = Views.findById(this, R.id.login_title);
        errorView = Views.findById(this, R.id.login_error);
        progressView = Views.findById(this, R.id.login_progress);
        usernameView = Views.findById(this, R.id.login_username);
        passwordView = Views.findById(this, R.id.login_password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validate()) {
                        login();
                        return true;
                    }
                }
                return false;
            }
        });
        cancelView = Views.findById(this, R.id.login_cancel);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loginView = Views.findById(this, R.id.login_login);
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    login();
                }
            }
        });

    }

    private boolean validate() {
        boolean isUsernameValid = inputFieldValidator.isValid(getUsername());
        boolean isPasswordValid = inputFieldValidator.isValid(getPassword());
        if (!isPasswordValid || !isUsernameValid) {
            showError();
        }
        return isUsernameValid && isPasswordValid;
    }

    private void showError() {
        errorView.setVisibility(View.VISIBLE);
    }

    private void login() {
        showProgress();
        Provider provider = Inject.provider();
        subscription = provider
                .observeLogin(getUsername(), getPassword())
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
                        Inject.crashAnalytics().logSomethingWentWrong("Provider: login: " + getUsername(), e);
                    }

                    @Override
                    public void onNext(Login.Status status) {
                        if (status == Login.Status.SUCCESSFUL) {
                            showSuccess();
                            navigate().toNews();
                        } else {
                            hideProgress();
                            showError();
                        }
                    }
                });

    }

    protected String getPassword() {
        return passwordView.getText().toString();
    }

    protected String getUsername() {
        return usernameView.getText().toString();
    }

    private void showSuccess() {
        String message = String.format(getResources().getString(R.string.navigation_drawer_welcome), getUsername());
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void showProgress() {
        titleView.setText(R.string.title_checking_account);
        progressView.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        titleView.setText(R.string.title_add_account);
        progressView.setVisibility(View.INVISIBLE);
    }

}
