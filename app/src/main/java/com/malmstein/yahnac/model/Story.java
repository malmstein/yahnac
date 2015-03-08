package com.malmstein.yahnac.model;

import android.database.Cursor;
import android.text.TextUtils;

import com.malmstein.yahnac.data.HNewsContract;

import java.io.Serializable;

public class Story implements Serializable {

    public static String COMMENT_URL_BASE = "https://news.ycombinator.com/item?id=";
    public static String NEXT_URL_BASE = "https://news.ycombinator.com/";
    public static String ASK_URL_BASE = "item?id=";

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
    private final String timeAgo;
    private final int score;
    private final String title;
    private final String url;
    private final String domain;
    private final int comments;
    private final String type;
    private final Long timestamp;
    private final int rank;
    private final int bookmark;

    public Story(Long internalId, String by, Long id, String type, String timeAgo, int score, String title, String url, String domain, int comments, Long timestamp, int rank, int bookmark) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.timeAgo = timeAgo;
        this.type = type;
        this.score = score;
        this.title = title;
        this.url = url;
        this.domain = domain;
        this.comments = comments;
        this.timestamp = timestamp;
        this.rank = rank;
        this.bookmark = bookmark;
    }

    public Long getInternalId() {
        return internalId;
    }

    public String getSubmitter() {
        return by;
    }

    private boolean isSubmitterEmpty() {
        return TextUtils.isEmpty(by);
    }

    public boolean isStoryAJob() {
        return isSubmitterEmpty();
    }

    public Long getId() {
        return id;
    }

    public String getTimeAgo() {
        return timeAgo;
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

    public int getComments() {
        return comments;
    }

    public String getDomain() {
        return domain;
    }

    public boolean hasDomain() {
        return !TextUtils.isEmpty(domain);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getCommentsUrl() {
        return COMMENT_URL_BASE + getId();
    }

    public int getRank() {
        return rank;
    }

    public boolean isHackerNewsLocalItem() {
        boolean isLocalItem = false;
        if (getType().equals(TYPE.ask)) {
            isLocalItem = true;
        }

        if (url.startsWith(ASK_URL_BASE)) {
            isLocalItem = true;
        }

        return isLocalItem;
    }

    public boolean isBookmark() {
        return bookmark == 0 ? false : true;
    }

    public static Story from(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.COLUMN_ID);
        Long id = cursor.getLong(HNewsContract.COLUMN_ITEM_ID);
        String type = cursor.getString(HNewsContract.COLUMN_TYPE);
        String by = cursor.getString(HNewsContract.COLUMN_BY);
        int comments = cursor.getInt(HNewsContract.COLUMN_COMMENTS);
        String domain = cursor.getString(HNewsContract.COLUMN_DOMAIN);
        String url = cursor.getString(HNewsContract.COLUMN_URL);
        int score = cursor.getInt(HNewsContract.COLUMN_SCORE);
        String title = cursor.getString(HNewsContract.COLUMN_TITLE);
        String time = cursor.getString(HNewsContract.COLUMN_TIME_AGO);
        Long timestamp = cursor.getLong(HNewsContract.COLUMN_TIMESTAMP);
        int rank = cursor.getInt(HNewsContract.COLUMN_RANK);
        int bookmark = cursor.getInt(HNewsContract.COLUMN_BOOKMARK);

        return new Story(internalId, by, id, type, time, score, title, url, domain, comments, timestamp, rank, bookmark);
    }
}