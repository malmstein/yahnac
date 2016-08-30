package com.malmstein.yahnac.model;

import android.database.Cursor;
import android.text.Html;

import com.malmstein.yahnac.data.HNewsContract;

public class Comment {

    private final Long internalId;
    private final String by;
    private final Long id;
    private final int level;
    private final String text;
    private final String timeText;
    private final boolean isHeader;
    private final Long commentId;

    public Comment(Long internalId, String by, Long id, int level, String text, String timeText, boolean isHeader, Long commentId) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.level = level;
        this.text = text;
        this.timeText = timeText;
        this.isHeader = isHeader;
        this.commentId = commentId;
    }

    public static Comment from(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.CommentsEntry.COLUMN_ID);
        Long id = cursor.getLong(HNewsContract.CommentsEntry.COLUMN_ITEM_ID);
        String by = cursor.getString(HNewsContract.CommentsEntry.COLUMN_BY);
        int level = cursor.getInt(HNewsContract.CommentsEntry.COLUMN_LEVEL);
        String text = cursor.getString(HNewsContract.CommentsEntry.COLUMN_TEXT);
        String timeText = cursor.getString(HNewsContract.CommentsEntry.COLUMN_TIME_AGO);
        boolean isHeader = cursor.getInt(HNewsContract.CommentsEntry.COLUMN_HEADER) == HNewsContract.TRUE_BOOLEAN;
        Long commentId = cursor.getLong(HNewsContract.CommentsEntry.COLUMN_COMMENT_ID);

        return new Comment(internalId, by, id, level, text, timeText, isHeader, commentId);
    }

    public String getText() {
        return text;
    }

    /**
     * Returns displayable styled comment text.
     */
    public CharSequence getStyledText() {
        // First transform the HTML String into a styled CharSequence.
        final CharSequence styledText = Html.fromHtml(getText());

        // Now remove optional trailing newline characters.
        int end = styledText.length();
        while (end >= 0 && styledText.charAt(end - 1) == '\n') {
            end--;
        }

        if (end != styledText.length()) {
            return styledText.subSequence(0, end);
        } else {
            return styledText;
        }
    }

    public int getLevel() {
        return level;
    }

    public String getTimeText() {
        return timeText;
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

    public boolean isHeader() {
        return isHeader;
    }

    public Long getCommentId() {
        return commentId;
    }
}
