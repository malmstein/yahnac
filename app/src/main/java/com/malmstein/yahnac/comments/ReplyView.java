package com.malmstein.yahnac.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.data.Provider;
import com.malmstein.yahnac.injection.Inject;
import com.malmstein.yahnac.login.InputFieldValidator;
import com.malmstein.yahnac.model.OperationResponse;
import com.novoda.notils.caster.Views;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ReplyView extends FrameLayout {

    private InputFieldValidator inputFieldValidator = new InputFieldValidator();

    private View cancel;
    private View reply;
    private EditText comment;

    private Listener listener;
    private long storyId = 0;
    private long commentId = 0;

    private Subscription subscription;

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

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReplyCancelled();
                }
            }
        });

        reply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (validate()) {
                        sendReply();
                    }
                }
            }
        });
    }

    private void sendReply() {
        if (commentId != 0) {
            sendReplytoComment();
        } else {
            sendReplytoStory();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    private boolean validate() {
        String commentText = comment.getText().toString();
        boolean isCommentValid = inputFieldValidator.isValid(commentText);
        return isCommentValid;
    }

    private void sendReplytoStory() {
        Provider provider = Inject.provider();
        subscription = provider
                .observeCommentOnStory(storyId, comment.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OperationResponse>() {
                    @Override
                    public void onCompleted() {
                        if (!subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Inject.crashAnalytics().logSomethingWentWrong("Send Comment: ", e);
                    }

                    @Override
                    public void onNext(OperationResponse status) {
                        listener.onReplySuccessful();
                    }
                });
    }

    private void sendReplytoComment() {
        Provider provider = Inject.provider();
        subscription = provider
                .observeReplyToComment(storyId, commentId, comment.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OperationResponse>() {
                    @Override
                    public void onCompleted() {
                        if (!subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Inject.crashAnalytics().logSomethingWentWrong("Send Comment: ", error);
                    }

                    @Override
                    public void onNext(OperationResponse status) {
                        if (status == OperationResponse.LOGIN_EXPIRED) {
                            listener.onLoginExpired();
                        } else {
                            listener.onReplySuccessful();
                        }
                    }
                });
    }

    public void clearAndHide() {
        comment.setText("");
        setVisibility(View.GONE);
    }

    public interface Listener {
        void onReplyCancelled();

        void onReplySuccessful();

        void onLoginExpired();

    }
}
