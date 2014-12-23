package com.malmstein.hnews.stories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.comments.CommentFragment;
import com.malmstein.hnews.comments.CommentsActivity;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.settings.SettingsActivity;

public class TopStoriesActivity extends HNewsActivity implements TopStoriesFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShareClicked(Intent shareIntent) {
        startActivity(shareIntent);
    }

    @Override
    public void onCommentsClicked(Long internalId) {
        startActivity(new Intent(this, CommentsActivity.class).putExtra(CommentFragment.ARG_STORY_ID, internalId));
    }

    @Override
    public void onContentClicked(Story story) {
        navigateToArticle(story);
    }

    private void navigateToArticle(Story story){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean preferExternalBrowser = preferences.getBoolean(getString(R.string.pref_enable_browser_key), Boolean.valueOf(getString(R.string.pref_enable_browser_default)));
        if (preferExternalBrowser){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(story.getUrl()));
            startActivity(browserIntent);
        } else {
            startActivity(new Intent(this, ArticleActivity.class).putExtra(ArticleFragment.ARG_STORY_ID, story.getInternalId()));
        }

    }

}
