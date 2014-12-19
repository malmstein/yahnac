package com.malmstein.hnews.stories;

import android.content.Context;
import android.util.AttributeSet;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.views.RelativeLayoutWithForeground;

public class ArticleHeaderView extends RelativeLayoutWithForeground {

    public ArticleHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArticleHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.view_article_list_item, this);
    }

    public void updateWith(Story story){

    }


}
