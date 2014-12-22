package com.malmstein.hnews.stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;
import com.malmstein.hnews.comments.CommentFragment;
import com.malmstein.hnews.comments.CommentsActivity;

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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
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
    public void onContentClicked(Long internalId) {
        startActivity(new Intent(this, ArticleActivity.class).putExtra(ArticleFragment.ARG_STORY_ID, internalId));
    }

}
