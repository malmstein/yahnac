package com.malmstein.hnews.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HNewsSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static HNewsSyncAdapter sSunshineSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new HNewsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}