package com.malmstein.yahnac.comments.parser;

import android.content.ContentValues;

import com.malmstein.yahnac.data.HNewsContract;

import java.util.Vector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CommentsParser {

    private final Document document;
    private final Long storyId;

    private Vector<ContentValues> commentsList = new Vector<>();

    public CommentsParser(Long storyId, Document document) {
        this.storyId = storyId;
        this.document = document;
    }

    public Vector<ContentValues> parse() {
        commentsList.clear();

        Elements tableRows = document.select("table tr table tr:has(table)");

        storesQuestion();

        for (int row = 0; row < tableRows.size(); row++) {
            Element mainRowElement = tableRows.get(row).select("td:eq(2)").first();
            Element rowLevelElement = tableRows.get(row).select("td:eq(0)").first();

            if (mainRowElement == null) {
                continue;
            }

            String text = parseText(mainRowElement);
            String commentId = parseCommentId(mainRowElement);
            String author = parseAuthor(mainRowElement);
            String timeAgo = parseTimeAgo(mainRowElement);
            int level = parseLevel(rowLevelElement);

            ContentValues commentValues = new ContentValues();

            commentValues.put(HNewsContract.CommentsEntry.BY, author);
            commentValues.put(HNewsContract.CommentsEntry.ITEM_ID, storyId);
            commentValues.put(HNewsContract.CommentsEntry.TEXT, text);
            commentValues.put(HNewsContract.CommentsEntry.LEVEL, level);
            commentValues.put(HNewsContract.CommentsEntry.TIME_AGO, timeAgo);
            commentValues.put(HNewsContract.CommentsEntry.HEADER, System.currentTimeMillis());
            commentValues.put(HNewsContract.CommentsEntry.COMMENT_ID, commentId);

            commentsList.add(commentValues);
        }

        return commentsList;
    }

    public String parseText(Element topRowElement) {
        Element commentSpan = topRowElement.select("div.comment > span").first();
        if (commentSpan == null) {
            return "";
        }
        commentSpan.select("div.reply").remove();

        String text = commentSpan.html().replace("<span> </span>", "");
        return text;
    }

    public String parseCommentId(Element topRowElement) {
        Element comHeadElement = topRowElement.select("span.comhead").first();
        String item_i = comHeadElement.select("a[href*=item]").attr("href");
        String commentId = item_i.replace("item?id=", "");

        return commentId;
    }

    public String parseAuthor(Element topRowElement) {
        Element comHeadElement = topRowElement.select("span.comhead").first();
        String author = comHeadElement.select("a[href*=user]").text();
        return author;
    }

    public String parseTimeAgo(Element topRowElement) {
        Element comHeadElement = topRowElement.select("span.comhead").first();
        String timeAgo = comHeadElement.select("a[href*=item").text();
        return timeAgo;
    }

    public int parseLevel(Element rowLevelElement) {
        String levelSpacerWidth = rowLevelElement.select("img").first().attr("width");
        int level = 0;
        if (levelSpacerWidth != null) {
            level = Integer.parseInt(levelSpacerWidth);
        }
        return level;
    }

    public String parseVoteUrl(Element voteElement) {
        if (voteElement != null) {
            return voteElement.attr("href").contains("auth=") ?
                    (voteElement.attr("href")) : null;
        }

        return null;
    }

    public void storesQuestion() {
        Element headerElement = document.select("body table:eq(0)  tbody > tr:eq(2) > td:eq(0) > table").get(0);

        if (headerElement.select("tr").size() > 5) {
            Elements headerRows = headerElement.select("tr");
            if (headerRows.size() == 6) {
                String header = headerRows.get(3).select("td").get(1).html();

                ContentValues commentValues = new ContentValues();

                commentValues.put(HNewsContract.CommentsEntry.ITEM_ID, storyId);
                commentValues.put(HNewsContract.CommentsEntry.TEXT, header);
                commentValues.put(HNewsContract.CommentsEntry.HEADER, HNewsContract.TRUE_BOOLEAN);

                commentsList.add(commentValues);

            }
        }
    }
}

