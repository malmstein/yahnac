package com.malmstein.yahnac;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.malmstein.yahnac.data.connectivity.NetworkChecker;

public class HNewsFragment extends Fragment {

    private NetworkChecker networkChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNetworkChecker();
    }

    private void initNetworkChecker() {
        networkChecker = new NetworkChecker(getActivity());
    }

    protected boolean isOnline() {
        return networkChecker.isConnected();
    }
}
