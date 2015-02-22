package com.malmstein.hnews.comments;

import android.content.ContentValues;

import com.malmstein.hnews.data.HNewsContract;

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
            String author = parseAuthor(mainRowElement);
            String timeAgo = parseTimeAgo(mainRowElement);
            int level = parseLevel(rowLevelElement);

            ContentValues commentValues = new ContentValues();

            commentValues.put(HNewsContract.ItemEntry.COLUMN_BY, author);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, storyId);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_TEXT, text);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_LEVEL, level);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_TIME_AGO, timeAgo);

            commentsList.add(commentValues);
        }

        return commentsList;
    }

    public String parseText(Element topRowElement) {
        String text = topRowElement.select("span.comment > *:not(:has(font[size=1]))").html();
        return text;
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

    public void storesQuestion() {
        Element headerElement = document.select("body table:eq(0)  tbody > tr:eq(2) > td:eq(0) > table").get(0);

        if (headerElement.select("tr").size() > 5) {
            Elements headerRows = headerElement.select("tr");
            if (headerRows.size() == 6) {
                String header = headerRows.get(3).select("td").get(1).html();

                ContentValues commentValues = new ContentValues();

                commentValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, storyId);
                commentValues.put(HNewsContract.ItemEntry.COLUMN_TEXT, header);
                commentValues.put(HNewsContract.ItemEntry.COLUMN_HEADER, HNewsContract.TRUE_BOOLEAN);

                commentsList.add(commentValues);

            }
        }
    }
}

