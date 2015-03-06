package com.malmstein.hnews.stories;

import android.content.Intent;
import android.database.Cursor;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.data.HNewsContract;
import com.malmstein.hnews.model.Story;

import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class ArticleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "ArticleFragment";

    public static final String ARG_STORY = BuildConfig.APPLICATION_ID + ".ARG_STORY";
    public static final String ARG_STORY_TITLE = BuildConfig.APPLICATION_ID + ".ARG_STORY_TITLE";

    private static final int ARTICLE_LOADER = 0;

    private ShareActionProvider mShareActionProvider;
    private WebView webView;
    private ProgressBar webViewProgress;
    private String articleUrl;

    public static ArticleFragment from(Story story) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY, story.getInternalId());
        args.putSerializable(ARG_STORY_TITLE, story.getTitle());
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Long getItemId() {
        if (getArguments().containsKey(ARG_STORY)) {
            return getArguments().getLong(ARG_STORY);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    private String getStoryTitle() {
        if (getArguments().containsKey(ARG_STORY_TITLE)) {
            return getArguments().getString(ARG_STORY_TITLE);
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, articleUrl);
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);

        webView = (WebView) rootView.findViewById(R.id.article_webview);
        webViewProgress = (ProgressBar) rootView.findViewById(R.id.article_progress);

        setupWebView();

        return rootView;
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

    }

    private class HackerNewsWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_STORY)) {
            getLoaderManager().restartLoader(ARTICLE_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri storyUri = HNewsContract.ItemEntry.buildStoryUriWith(getItemId());

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
            getActivity().setTitle(getStoryTitle());
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareArticleIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
