package com.malmstein.hnews.model;

import android.content.ContentValues;

import java.util.Vector;

public class CommentsJsoup {

    private String title = "";
    private Vector<ContentValues> commentsList = new Vector<>();

    public CommentsJsoup() {

    }

    public CommentsJsoup(String title, Vector<ContentValues> commentsList) {
        this.title = title;
        this.commentsList = commentsList;
    }

    public String getTitle() {
        return title;
    }

    public Vector<ContentValues> getCommentsList() {
        return commentsList;
    }
}
