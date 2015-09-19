package com.malmstein.yahnac.login;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.malmstein.yahnac.R;
import com.novoda.notils.caster.Views;

public class LoginView extends FrameLayout {

    private static final int IME_FLAGS_NONE = 0;

    private InputFieldValidator inputFieldValidator = new InputFieldValidator();

    private EditText usernameView;
    private EditText passwordView;
    private TextView cancelButon;
    private TextView loginButton;
    private View loginButtonsContainer;
    private TextView errorTextView;

    private View scrollContainer;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static Editable createEmptyEditable() {
        return new Editable.Factory().newEditable("");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.merge_login_view, this, true);
        usernameView = Views.findById(this, R.id.login_username);
        passwordView = Views.findById(this, R.id.login_password);
        loginButtonsContainer = Views.findById(this, R.id.login_buttons_container);
        cancelButon = Views.findById(this, R.id.login_cancel);
        loginButton = Views.findById(this, R.id.login_login);

        errorTextView = Views.findById(this, R.id.login_error_label);
        scrollContainer = findViewById(R.id.login_container);

    }

    public void bind(Listener listener) {
        updatePasswordView(listener);
        updateCancelButton(listener);
        updateNextButton(listener);
    }

    private void updatePasswordView(final Listener listener) {
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validate()) {
                        listener.onSignIn(getUsername(), getPassword());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void updateCancelButton(final Listener listener) {
        cancelButon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
            }
        });

    }

    private void updateNextButton(final Listener listener) {
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearError();
                if (validate()) {
                    onSignInClicked(v, listener);
                }
            }
        });
    }

    public void clearError() {
        errorTextView.setVisibility(INVISIBLE);
    }

    private boolean validate() {
        boolean isUsernameValid = inputFieldValidator.isValid(getUsername());
        boolean isPasswordValid = inputFieldValidator.isValid(getPassword());
        if (!isPasswordValid || !isUsernameValid) {
            showError();
        }
        return isUsernameValid && isPasswordValid;
    }

    private void onSignInClicked(View v, Listener listener) {
        v.clearFocus();
        hideKeyboardFor(v);
        listener.onSignIn(getUsername(), getPassword());
    }

    private void hideKeyboardFor(View editText) {
        if (isInEditMode()) {
            return;
        }

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), IME_FLAGS_NONE);
    }

    protected String getUsername() {
        return usernameView.getText().toString();
    }

    protected String getPassword() {
        return passwordView.getText().toString();
    }

    public void showProgress() {
        scrollContainer.setVisibility(GONE);
        loginButtonsContainer.setVisibility(GONE);
    }

    public void hideProgress() {
        scrollContainer.setVisibility(VISIBLE);
        loginButtonsContainer.setVisibility(VISIBLE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            return;
        }

        TextChangedListener textChangedListener = new TextChangedListener(this);
        setupSignupModeChangeListener(textChangedListener);
        textChangedListener.afterTextChanged(createEmptyEditable());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setupSignupModeChangeListener(null);
    }

    public void showError() {
        errorTextView.setVisibility(VISIBLE);
    }

    private void setupSignupModeChangeListener(TextChangedListener listener) {
        usernameView.addTextChangedListener(listener);
        passwordView.addTextChangedListener(listener);
    }

    private boolean areBothLoginFieldsFilledIn() {
        return !TextUtils.isEmpty(usernameView.getText()) && !TextUtils.isEmpty(passwordView.getText());
    }

    private void onReadyToSigninStateChanged(boolean readyToSignin) {
        loginButton.setEnabled(readyToSignin);
    }

    public interface Listener {

        void onSignIn(String username, String password);

        void onCancel();

    }

    private static class TextChangedListener implements TextWatcher {

        private final LoginView parentView;
        private boolean wasReadyToSignin = false;

        TextChangedListener(LoginView parentView) {
            this.parentView = parentView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No-op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No-op
        }

        @Override
        public void afterTextChanged(Editable notUsed) {
            boolean readyToSignin = parentView.areBothLoginFieldsFilledIn();
            if (readyToSignin != wasReadyToSignin) {
                parentView.onReadyToSigninStateChanged(readyToSignin);
                wasReadyToSignin = readyToSignin;
            }
        }

    }

}
