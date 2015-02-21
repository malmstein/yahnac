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

    static Vector<ContentValues> commentsList = new Vector<>();

    public CommentsParser(Long storyId, Document document) {
        this.storyId = storyId;
        this.document = document;
    }

    public Vector<ContentValues> parse() {
        commentsList.clear();
        Elements tableRows = document.select("table tr table tr:has(table)");

//         String currentUser = Settings.getUserName(App.getInstance());

        String text = null;
        String author = null;
        int level = 0;
        String timeAgo = null;
        String url = null;
        Boolean isDownvoted = false;
        String upvoteUrl = null;
        String downvoteUrl = null;

        boolean endParsing = false;
        for (int row = 0; row < tableRows.size(); row++) {
            Element mainRowElement = tableRows.get(row).select("td:eq(2)").first();
            Element rowLevelElement = tableRows.get(row).select("td:eq(0)").first();
            if (mainRowElement == null)
                continue;

            text = mainRowElement.select("span.comment > *:not(:has(font[size=1]))").html();

            Element comHeadElement = mainRowElement.select("span.comhead").first();
            author = comHeadElement.select("a[href*=user]").text();
            timeAgo = comHeadElement.select("a[href*=item").text();
            Element urlElement = comHeadElement.select("a[href*=item]").first();
            if (urlElement != null)
                url = urlElement.attr("href");

            String levelSpacerWidth = rowLevelElement.select("img").first().attr("width");
            if (levelSpacerWidth != null)
                level = Integer.parseInt(levelSpacerWidth);

            ContentValues commentValues = new ContentValues();

            commentValues.put(HNewsContract.ItemEntry.COLUMN_BY, author);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, storyId);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_TEXT, text);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_LEVEL, level);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_TIME_AGO, timeAgo);

            commentsList.add(commentValues);

            if (endParsing)
                break;
        }

        Element header = document.select("body table:eq(0)  tbody > tr:eq(2) > td:eq(0) > table").get(0);

        if (header.select("tr").size() > 5) {
            String headerText = parseHeader(header);
        }

        return commentsList;
    }

    public String parseHeader(Element doc){
        Elements headerRows = doc.select("tr");

        // Six rows means that this is just a Ask HN post or a poll with
        // no options.  In either case, the content we want is in the fourth row
        if (headerRows.size() == 6) {
            return headerRows.get(3).select("td").get(1).html();
        }

        return null;
    }

}
