package com.malmstein.yahnac.data.connectivity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkChecker {

    private final ConnectivityManager connectivityManager;
    private final TelephonyManager telephonyManager;

    public NetworkChecker(Activity activity) {
        this.connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public boolean isConnected() {
        return isConnectedToCellNetwork() || isConnectedToWifiNetwork();
    }

    private boolean isConnectedToCellNetwork() {
        int simState = telephonyManager.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    private boolean isConnectedToWifiNetwork() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }

}
