package com.malmstein.yahnac.presenters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malmstein.yahnac.R;
import com.malmstein.yahnac.model.Comment;
import com.malmstein.yahnac.updater.LoginSharedPreferences;
import com.novoda.notils.caster.Views;

public class CommentsAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {

    private static int TYPE_HEADER = 0;
    private static int TYPE_COMMENT = 1;
    private final String type;
    private final Listener listener;
    private LoginSharedPreferences loginSharedPreferences;

    public CommentsAdapter(String type, Cursor cursor, Listener listener) {
        super(cursor);
        this.type = type;
        this.listener = listener;
        this.loginSharedPreferences = LoginSharedPreferences.newInstance();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_COMMENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return createHeaderHolder(parent);
        } else {
            return createCommentHolder(parent);
        }
    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
        Comment comment = Comment.from(cursor);

        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).bind(comment, loginSharedPreferences, listener);
        } else {
            ((HeaderViewHolder) holder).bind(comment, loginSharedPreferences, listener, type);
        }

    }

    private RecyclerView.ViewHolder createHeaderHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment_header, parent, false);
        HeaderViewHolder vh = new HeaderViewHolder(v);
        return vh;
    }

    private RecyclerView.ViewHolder createCommentHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        CommentViewHolder vh = new CommentViewHolder(v);
        return vh;
    }

    public interface Listener {
        void onCommentReplyAction();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final View comment_header;
        public final TextView text;
        public final TextView author;
        public final TextView when;
        public final View root;
        public final View footer;
        public final View reply;
        public final View vote;

        public HeaderViewHolder(View view) {
            super(view);
            comment_header = Views.findById(view, R.id.comment_header);
            text = Views.findById(view, R.id.comment_header_text);
            author = Views.findById(view, R.id.comment_header_by);
            when = Views.findById(view, R.id.comment_header_when);
            root = Views.findById(view, R.id.comment_header_root);
            footer = Views.findById(view, R.id.comment_footer);
            reply = Views.findById(view, R.id.comment_reply_action);
            vote = Views.findById(view, R.id.comment_vote_action);
        }

        public void bind(Comment comment, LoginSharedPreferences loginSharedPreferences, final Listener listener, String type) {
            text.setText(Html.fromHtml(comment.getText()));
            text.setMovementMethod(LinkMovementMethod.getInstance());
            if (comment.isHeader() || type.equals("ask")) {
                comment_header.setVisibility(View.GONE);
            } else {
                author.setText(comment.getBy());
                when.setText(comment.getTimeText());
            }
            if (loginSharedPreferences.isLoggedIn()) {
                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCommentReplyAction();
                    }
                });
            } else {
                footer.setVisibility(View.GONE);
            }
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final TextView author;
        public final TextView when;
        public final View root;
        public final View footer;
        public final View vote;
        public final View reply;

        public CommentViewHolder(View view) {
            super(view);
            text = Views.findById(view, R.id.comment_text);
            author = Views.findById(view, R.id.comment_by);
            when = Views.findById(view, R.id.comment_when);
            root = Views.findById(view, R.id.comment_root);
            footer = Views.findById(view, R.id.comment_footer);
            vote = Views.findById(view, R.id.comment_vote_action);
            reply = Views.findById(view, R.id.comment_reply_action);
        }

        public void bind(Comment comment, LoginSharedPreferences loginSharedPreferences, final Listener listener) {
            text.setText(Html.fromHtml(comment.getText()));
            text.setMovementMethod(LinkMovementMethod.getInstance());
            author.setText(comment.getBy());
            when.setText(comment.getTimeText());
            if (loginSharedPreferences.isLoggedIn()) {
                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCommentReplyAction();
                    }
                });
            } else {
                footer.setVisibility(View.GONE);
            }
        }
    }

}
