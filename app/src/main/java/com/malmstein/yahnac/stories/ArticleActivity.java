package com.malmstein.yahnac.stories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.malmstein.yahnac.BuildConfig;
import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.model.Story;
import com.novoda.notils.caster.Views;

public class ArticleActivity extends HNewsActivity {

    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_STORY";

    private ShareActionProvider mShareActionProvider;
    private WebView webView;
    private ProgressBar webViewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);

        setupSubActivityWithTitle();
        setTitle(getStory().getTitle());

        webView = Views.findById(this, R.id.article_webview);
        webViewProgress = Views.findById(this, R.id.article_progress);

        setupWebView();
    }

    private Story getStory() {
        return (Story) getIntent().getExtras().getSerializable(ARG_STORY);
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareArticleIntent());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_comments) {
            navigate().toComments(getStory());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareArticleIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getStory().getUrl());
        return shareIntent;
    }

    private class HackerNewsWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
