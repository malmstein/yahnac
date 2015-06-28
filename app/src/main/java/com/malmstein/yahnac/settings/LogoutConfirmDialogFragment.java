package com.malmstein.yahnac.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.malmstein.yahnac.R;

public class LogoutConfirmDialogFragment extends DialogFragment {

    public static LogoutConfirmDialogFragment newInstance() {
        return new LogoutConfirmDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Listener listener = (Listener) getActivity();
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.confirm_logout_message))
                .setPositiveButton(R.string.action_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onLogoutConfirmed();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();
    }

    public interface Listener {

        void onLogoutConfirmed();

    }

}
