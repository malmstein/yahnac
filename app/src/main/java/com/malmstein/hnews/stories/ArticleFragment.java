package com.malmstein.hnews.stories;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.data.HNewsContract;
import com.novoda.notils.logger.simple.Log;

import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class ArticleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "ArticleFragment";
    public static final String ARG_STORY_ID = BuildConfig.APPLICATION_ID + ".ARG_STORY_ID";
    private static final int ARTICLE_LOADER = 0;

    private ShareActionProvider mShareActionProvider;
    private WebView webView;
    private String articleUrl;

    public static ArticleFragment from(Long itemId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY_ID, itemId);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Long getItemId() {
        if (getArguments().containsKey(ARG_STORY_ID)) {
            return getArguments().getLong(ARG_STORY_ID);
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
        inflater.inflate(R.menu.menu_article, menu);
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, articleUrl);
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);

        webView = (WebView) rootView.findViewById(R.id.article_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new HackerNewsWebClient());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_STORY_ID)) {
            getLoaderManager().restartLoader(ARTICLE_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyUri = HNewsContract.ItemEntry.buildItemUri(getItemId());

        return new CursorLoader(
                getActivity(),
                storyUri,
                STORY_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            articleUrl = data.getString(data.getColumnIndex(HNewsContract.ItemEntry.COLUMN_URL));
            webView.loadUrl(articleUrl);
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareArticleIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class HackerNewsWebClient extends WebViewClient {

        boolean loadedFinished = false;

        @Override
        public void onPageFinished(WebView view, String url) {
            loadedFinished = true;
            Log.d("onPageFinished: " + url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (loadedFinished && (!articleUrl.equals(url))) {
                launchExternalBrowser(Uri.parse(url));
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (loadedFinished && (!articleUrl.equals(url))) {
                launchExternalBrowser(Uri.parse(url));
                return true;
            }

            return false;
        }

        private void launchExternalBrowser(Uri newUri) {
            Intent intent = new Intent(Intent.ACTION_VIEW, newUri);
            startActivity(intent);
        }
    }
}
