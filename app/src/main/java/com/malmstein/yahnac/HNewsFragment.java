package com.malmstein.yahnac;

import android.support.v4.app.Fragment;

import com.malmstein.yahnac.connectivity.NetworkDetector;
import com.malmstein.yahnac.views.toolbar.HNToolbar;

public class HNewsFragment extends Fragment {

    public HNToolbar getToolbar(){
        return ((HNewsActivity) getActivity()).getAppBar();
    }

    protected boolean isOnline() {
        NetworkDetector networkDetector = new NetworkDetector(getActivity());
        return networkDetector.isDataConnectionAvailable();
    }
}
