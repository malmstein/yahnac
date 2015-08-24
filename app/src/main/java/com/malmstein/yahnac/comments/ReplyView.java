package com.malmstein.yahnac.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.malmstein.yahnac.R;
import com.novoda.notils.caster.Views;

public class ReplyView extends FrameLayout {

    private View cancel;
    private View reply;
    private EditText comment;

    public ReplyView(Context context) {
        super(context);
    }

    public ReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReplyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReplyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.merge_reply_view, this, true);
        cancel = Views.findById(this, R.id.reply_cancel);
        reply = Views.findById(this, R.id.reply_send);
        comment = Views.findById(this, R.id.reply_comment);
    }
}
