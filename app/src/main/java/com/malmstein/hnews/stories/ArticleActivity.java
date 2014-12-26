package com.malmstein.hnews.stories;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;

public class ArticleActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);

        setupSubActivityWithTitle();

        if (findArticleFragment() == null) {
            Long storyId = getIntent().getExtras().getLong(ArticleFragment.ARG_STORY_ID);
            String title = getIntent().getExtras().getString(ArticleFragment.ARG_STORY_TITLE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.article_fragment_root, ArticleFragment.from(storyId, title), ArticleFragment.TAG).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    private ArticleFragment findArticleFragment() {
        return (ArticleFragment) getSupportFragmentManager().findFragmentByTag(ArticleFragment.TAG);
    }

}
