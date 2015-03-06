package com.malmstein.hnews;

import android.support.v4.app.Fragment;

import com.malmstein.hnews.connectivity.WizMerlin;
import com.malmstein.hnews.views.toolbar.HNToolbar;

public class HNewsFragment extends Fragment {

    public HNToolbar getToolbar(){
        return ((HNewsActivity) getActivity()).getAppBar();
    }

    public WizMerlin getMerlin() {
        return ((HNewsActivity) getActivity()).getMerlin();
    }

}
