package com.malmstein.yahnac.stories;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.malmstein.yahnac.HNewsActivity;
import com.malmstein.yahnac.R;
import com.malmstein.yahnac.model.Story;

public class ArticleActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);

        setupSubActivityWithTitle();

        if (findArticleFragment() == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.article_fragment_root, ArticleFragment.from(getStory()), ArticleFragment.TAG).commit();
        }

    }

    private Story getStory(){
        return (Story) getIntent().getExtras().getSerializable(ArticleFragment.ARG_STORY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_comments) {
            navigate().toComments(getStory());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArticleFragment findArticleFragment() {
        return (ArticleFragment) getSupportFragmentManager().findFragmentByTag(ArticleFragment.TAG);
    }

}
