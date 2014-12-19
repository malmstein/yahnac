package com.malmstein.hnews.stories;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;

public class ArticleActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);

        setToolbar();
        setupUpIndicatorOn();

        if (findArticleFragment() == null){
            Long itemId = getIntent().getExtras().getLong(ArticleFragment.ARG_STORY_ID);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.article_fragment_root, ArticleFragment.from(itemId), ArticleFragment.TAG).commit();
        }

    }

    private ArticleFragment findArticleFragment(){
        return (ArticleFragment) getSupportFragmentManager().findFragmentByTag(ArticleFragment.TAG);
    }

}
