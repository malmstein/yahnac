package com.malmstein.yahnac.stories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.base.DeveloperError;
import com.malmstein.yahnac.model.Story;

public class ArticleFragment extends Fragment {

    public static final String TAG = "ArticleFragment";

    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_STORY";

    private ShareActionProvider mShareActionProvider;
    private WebView webView;
    private ProgressBar webViewProgress;

    public static ArticleFragment from(Story story) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, story);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Story getStory() {
        if (getArguments().containsKey(ARG_STORY)) {
            return (Story) getArguments().getSerializable(ARG_STORY);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareArticleIntent());
        }
    }

    private Intent createShareArticleIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getStory().getUrl());
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);

        webView = (WebView) rootView.findViewById(R.id.article_webview);
        webViewProgress = (ProgressBar) rootView.findViewById(R.id.article_progress);

        setupWebView();
        getActivity().setTitle(getStory().getTitle());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareArticleIntent());
        }
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webView.setWebViewClient(new HackerNewsWebClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                webViewProgress.setProgress(progress);
                if (webViewProgress.getProgress() >= 100) {
                    webViewProgress.setVisibility(View.GONE);
                }
            }
        });

        webView.loadUrl(getStory().getUrl());
    }

    private class HackerNewsWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
