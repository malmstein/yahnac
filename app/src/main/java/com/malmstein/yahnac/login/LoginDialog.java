package com.malmstein.yahnac.login;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.DataRepository;
import com.malmstein.yahnac.inject.Inject;
import com.malmstein.yahnac.model.Login;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class LoginDialog extends DialogFragment {

    public static final String TAG = "LoginDialog";
    private Listener listener;

    private EditText usernameView;
    private EditText passwordView;
    private View errorView;
    private TextView titleView;
    private ProgressBar progressView;
    private View loginView;
    private View cancelView;

    private InputFieldValidator inputFieldValidator = new InputFieldValidator();
    private Subscription subscription;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (Listener) activity;
    }

    @Override
    public void onDetach() {
        this.listener = new DummyListener();
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleView = Views.findById(view, R.id.dialog_login_title);
        errorView = Views.findById(view, R.id.dialog_login_error);
        progressView = Views.findById(view, R.id.dialog_login_progress);
        usernameView = Views.findById(view, R.id.dialog_login_username);
        passwordView = Views.findById(view, R.id.dialog_login_password);
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
        cancelView = Views.findById(view, R.id.dialog_login_cancel);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onLoginCancelled();
            }
        });
        loginView = Views.findById(view, R.id.dialog_login_login);
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
        DataRepository dataRepository = Inject.dataRepository();
        subscription = dataRepository
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
                        Inject.crashAnalytics().logSomethingWentWrong("DataRepository: login: " + getUsername(), e);
                    }

                    @Override
                    public void onNext(Login.Status status) {
                        if (status == Login.Status.SUCCESSFUL) {
                            listener.onLoginSucceed();
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

    private void showProgress() {
        titleView.setText(R.string.title_checking_account);
        progressView.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        titleView.setText(R.string.title_add_account);
        progressView.setVisibility(View.INVISIBLE);
    }

    public interface Listener {
        void onLoginSucceed();
        void onLoginCancelled();
    }

    private static class DummyListener implements Listener {

        @Override
        public void onLoginSucceed() {
            // no-op
        }

        @Override
        public void onLoginCancelled() {
            // no-op
        }
    }
}
