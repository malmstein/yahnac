package com.malmstein.hnews.stories;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.malmstein.hnews.R;

public class ArticleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);

        Long itemId = getIntent().getExtras().getLong(ArticleFragment.ARG_STORY_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.article_fragment_root, ArticleFragment.from(itemId)).commit();

    }
}
