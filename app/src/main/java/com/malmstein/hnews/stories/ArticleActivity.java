package com.malmstein.hnews.stories;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.malmstein.hnews.R;

public class ArticleActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);

        setToolbar();

        Long itemId = getIntent().getExtras().getLong(ArticleFragment.ARG_STORY_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.article_fragment_root, ArticleFragment.from(itemId)).commit();

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setAppBarColor(getDefaultAppBarColor(getResources()));
    }

    private int getDefaultAppBarColor(Resources resources) {
        return resources.getColor(R.color.orange);
    }

    private void setAppBarColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }

}
