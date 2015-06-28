package com.malmstein.yahnac;

import android.support.v4.app.Fragment;

import com.malmstein.yahnac.base.Navigator;
import com.malmstein.yahnac.connectivity.NetworkDetector;

public class HNewsFragment extends Fragment {

    public Navigator navigate() {
        return ((HNewsActivity) getActivity()).navigate();
    }

    protected boolean isOnline() {
        NetworkDetector networkDetector = new NetworkDetector(getActivity());
        return networkDetector.isDataConnectionAvailable();
    }
}
