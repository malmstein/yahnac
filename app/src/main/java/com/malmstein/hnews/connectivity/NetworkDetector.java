package com.malmstein.hnews.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkDetector {

    Context context;

    public NetworkDetector(Context context) {
        this.context = context;
    }

    public NetworkInfo getActiveConnectionInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public boolean isDataConnectionAvailable() {
        NetworkInfo networkInfo = getActiveConnectionInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
