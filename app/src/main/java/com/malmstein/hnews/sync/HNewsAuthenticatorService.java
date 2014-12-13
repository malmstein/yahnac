package com.malmstein.hnews.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The service which allows the sync adapter framework to access the authenticator.
 */
public class HNewsAuthenticatorService extends Service {
    private HNewsAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new HNewsAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}