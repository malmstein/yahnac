package com.malmstein.yahnac.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.yahnac.HNewsFragment;
import com.malmstein.yahnac.R;
import com.novoda.notils.caster.Views;
import com.novoda.notils.widget.webview.RawWebView;

public class SoftwareLicensesFragment extends HNewsFragment {

    private RawWebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_software_licenses, container, false);
        webView = Views.findById(contentView, R.id.software_licenses_webview);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.loadRawResource(R.raw.software_licenses);
    }

}
