package com.malmstein.yahnac.story;

import android.content.Intent;
import android.os.Bundle;
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
import com.malmstein.yahnac.data.DataPersister;
import com.malmstein.yahnac.injection.Inject;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.views.SnackBarView;
import com.novoda.notils.caster.Views;

public class StoryActivity extends HNewsActivity {

    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_STORY";

    private WebView webView;
    private ProgressBar webViewProgress;

    private SnackBarView snackbarView;

    private int croutonAnimationDuration;
    private int croutonBackgroundAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_story);

        setupSubActivity();
        setupSnackbar();
        setupWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Inject.usageAnalytics().trackStory(
                getString(R.string.analytics_page_story), getStory());
    }

    private Story getStory() {
        return (Story) getIntent().getExtras().getSerializable(ARG_STORY);
    }

    private void setupSnackbar() {
        snackbarView = Views.findById(this, R.id.snackbar);
        croutonBackgroundAlpha = getResources().getInteger(R.integer.feed_crouton_background_alpha);
        croutonAnimationDuration = getResources().getInteger(R.integer.feed_crouton_animation_duration);
    }

    private void setupWebView() {
        webView = Views.findById(this, R.id.article_webview);
        webViewProgress = Views.findById(this, R.id.article_progress);

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

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
        MenuItem bookmarks = menu.findItem(R.id.action_bookmark);
        if (getStory().isBookmark()) {
            checkBookmarkMenuItem(bookmarks);
        } else {
            uncheckBookmarkMenuItem(bookmarks);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_comments:
                Inject.usageAnalytics().trackNavigateEvent(getString(R.string.analytics_event_view_comments_story),
                        getStory());
                navigate().toComments(getStory());
                finish();
                return true;
            case R.id.action_bookmark:
                onBookmarkClicked(item);
                return true;
            case R.id.action_share:
                Inject.usageAnalytics().trackShareEvent(getString(R.string.analytics_event_share_story),
                        getStory());
                Intent chooserIntent = Intent.createChooser(getStory().createShareIntent(), SHARE_DIALOG_DEFAULT_TITLE);
                startActivity(chooserIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void onBookmarkClicked(MenuItem item) {
        DataPersister persister = Inject.dataPersister();
        if (item.isChecked()) {
            Inject.usageAnalytics().trackBookmarkEvent(getString(R.string.analytics_event_remove_bookmark_story),
                    getStory());
            removeBookmark(persister, getStory());
            uncheckBookmarkMenuItem(item);
        } else {
            Inject.usageAnalytics().trackBookmarkEvent(getString(R.string.analytics_event_add_bookmark_story),
                    getStory());
            addBookmark(persister, getStory());
            checkBookmarkMenuItem(item);
        }
        getStory().toggleBookmark();
    }

    private void checkBookmarkMenuItem(MenuItem bookmarks) {
        bookmarks.setChecked(true);
        bookmarks.setIcon(R.drawable.ic_bookmark_white);
    }

    private void uncheckBookmarkMenuItem(MenuItem bookmarks) {
        bookmarks.setChecked(false);
        bookmarks.setIcon(R.drawable.ic_bookmark_outline_white);
    }

    private void removeBookmark(DataPersister persister, Story story) {
        persister.removeBookmark(story);
        showRemovedBookmarkSnackbar(persister, story);
    }

    private void addBookmark(DataPersister persister, Story story) {
        persister.addBookmark(story);
        showAddedBookmarkSnackbar(persister, story);
    }

    private void showAddedBookmarkSnackbar(final DataPersister persister, final Story story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_added_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Inject.usageAnalytics().trackBookmarkEvent(getString(R.string.analytics_event_remove_bookmark_story),
                                getStory());
                        snackbarView.hideCrouton();
                        removeBookmark(persister, story);
                        invalidateOptionsMenu();
                    }
                })
                .animating();
    }

    private void showRemovedBookmarkSnackbar(final DataPersister persister, final Story story) {
        snackbarView.showSnackBar(getResources().getText(R.string.feed_snackbar_removed_bookmark))
                .withBackgroundColor(R.color.black, croutonBackgroundAlpha)
                .withAnimationDuration(croutonAnimationDuration)
                .withUndoClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarView.hideCrouton();
                        addBookmark(persister, story);
                        invalidateOptionsMenu();
                    }
                })
                .animating();
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

        @Override
        public void onPageFinished(WebView view, String url) {
            String javascript = "javascript:document.getElementsByName('viewport')[0].setAttribute('content', 'initial-scale=1.0,maximum-scale=10.0');";
            view.loadUrl(javascript);
        }
    }

}
