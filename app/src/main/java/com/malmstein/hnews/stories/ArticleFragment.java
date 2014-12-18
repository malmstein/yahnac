package com.malmstein.hnews.stories;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.malmstein.hnews.BuildConfig;
import com.malmstein.hnews.R;
import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.data.HNewsContract;

import static com.malmstein.hnews.data.HNewsContract.STORY_COLUMNS;

public class ArticleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_STORY_ID = BuildConfig.APPLICATION_ID + ".ARG_STORY_ID";
    private static final int ARTICLE_LOADER = 0;
    private WebView webView;

    public static ArticleFragment from(Long itemId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY_ID, itemId);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Long getItemId(){
        if (getArguments().containsKey(ARG_STORY_ID)){
            return getArguments().getLong(ARG_STORY_ID);
        } else {
            throw new DeveloperError("Missing argument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);

        webView = (WebView) rootView.findViewById(R.id.article_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
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
            String url = data.getString(data.getColumnIndex(HNewsContract.ItemEntry.COLUMN_URL));
            webView.loadUrl(url);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class HackerNewsWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
//            if (Uri.parse(url).getHost().equals("www.example.com")) {
//                // This is my web site, so do not override; let my WebView load the page
//                return false;
//            }
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//            return true;
        }
    }
}
