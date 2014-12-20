package com.malmstein.hnews.comments;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.malmstein.hnews.HNewsActivity;
import com.malmstein.hnews.R;

public class CommentsActivity extends HNewsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setupSubActivityWithTitle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

}
