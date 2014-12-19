package com.malmstein.hnews.stories;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.malmstein.hnews.R;
import com.malmstein.hnews.model.Story;

public class ArticleHeader extends FrameLayout {

    public ArticleHeader(Context context) {
        super(context);
    }

    public ArticleHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArticleHeader(Context context, AttributeSet attrs, int defStyleAttr) {
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
