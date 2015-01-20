package com.malmstein.hnews.model;

import android.database.Cursor;

import com.malmstein.hnews.data.HNewsContract;

import java.io.Serializable;

public class Story implements Serializable {

    public String getDomain() {
        return domain;
    }

    public String getComments() {
        return comments;
    }

    public String getOrder() {
        return order;
    }

    public enum TYPE {
        top_story,
        new_story,
        best_story,
        show,
        ask
    }

    private final Long internalId;
    private final String by;
    private final Long id;
    private final Long time;
    private final int score;
    private final String title;
    private final String url;
    private final String domain;
    private final String comments;
    private final String type;
    private final String order;

    public Story(Long internalId, String by, Long id, String type, Long time, int score, String title, String url, String domain, String comments, String order) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.time = time;
        this.type = type;
        this.score = score;
        this.title = title;
        this.url = url;
        this.domain = domain;
        this.comments = comments;
        this.order = order;
    }

    public Long getInternalId() {
        return internalId;
    }

    public String getBy() {
        return by;
    }

    public Long getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public static Story from(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.COLUMN_ID);
        Long id = cursor.getLong(HNewsContract.COLUMN_ITEM_ID);
        String type = cursor.getString(HNewsContract.COLUMN_TYPE);
        String by = cursor.getString(HNewsContract.COLUMN_BY);
        String comments = cursor.getString(HNewsContract.COLUMN_COMMENTS);
        String domain = cursor.getString(HNewsContract.COLUMN_DOMAIN);
        String url = cursor.getString(HNewsContract.COLUMN_URL);
        int score = cursor.getInt(HNewsContract.COLUMN_SCORE);
        String title = cursor.getString(HNewsContract.COLUMN_TITLE);
        Long time = cursor.getLong(HNewsContract.COLUMN_TIME);
        String order = cursor.getString(HNewsContract.COLUMN_ITEM_ORDER);

        return new Story(internalId, by, id, type, time, score, title, url, domain, comments, order);
    }
}