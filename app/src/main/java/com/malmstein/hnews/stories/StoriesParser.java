package com.malmstein.hnews.stories;

import android.content.ContentValues;

import com.malmstein.hnews.data.HNewsContract;
import com.malmstein.hnews.model.Story;

import java.util.Vector;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StoriesParser {

    private final Document document;
    static Vector<ContentValues> storiesList = new Vector<>();

    public StoriesParser(Document document) {
        this.document = document;
    }

    public Vector<ContentValues> parse(Story.TYPE type) {
        storiesList.clear();

        Elements titles = document.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=title])");
        Elements subtext = document.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=subtext])");

        Integer i = 0;
        for (Element st : subtext) {

            String title = parseTitle(titles.get(i));
            String domain = parseDomain(titles.get(i));

            String points = parsePoints(st);
            String url = parseUrl(titles.get(i));
            String submitter = parseSubmitter(st);
            String ago = parseAgo(st);
            int comments = parseComments(st);
            int item_id = parseItemId(st);

//            String upvote_i = titles.get(i).select("td>center>a[href^=vote]").attr("href");

            i++;

            ContentValues storyValues = new ContentValues();

            storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, item_id);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TYPE, type.name());
            storyValues.put(HNewsContract.ItemEntry.COLUMN_BY, submitter);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TIME_AGO, ago);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_COMMENTS, comments);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_DOMAIN, domain);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_URL, url);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_SCORE, points);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TITLE, title);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_RANK, i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TIMESTAMP, System.currentTimeMillis());

            storiesList.add(storyValues);
        }

        return storiesList;
    }

    public String parseTitle(Element firstLine) {
        String title = firstLine.select("td[class=title]>a").text();
        return title;
    }

    public String parseDomain(Element firstLine) {
        String line = firstLine.select("td[class=title]>span").text();
        int start = line.indexOf("(");
        int end = line.indexOf(")");
        if (end == -1){
            return "";
        }
        String domain = line.substring(start, end + 1);
        return domain;
    }

    public String parsePoints(Element secondLine) {
        String points = secondLine.select("td[class=subtext]>span").text().replaceAll(" points", "").replaceAll(" point", "");
        return points;
    }

    public String parseUrl(Element firstLine) {
        String url = firstLine.select("td[class=title]>a").attr("href");
        return url;
    }

    public String parseSubmitter(Element secondLine) {
        String submitter = secondLine.select("a[href*=user]").text();
        return submitter;
    }

    public static String parseAgo(Element secondLine) {
        String ago;
        Elements secondLineElements = secondLine.select("a[href*=item]");
        if (secondLineElements.size() == 0) {
            ago = secondLine.text();
        } else {
            ago = secondLineElements.get(0).text();
        }
        return ago;
    }

    public static int parseComments(Element secondLine) {
        int comments = 0;
        Elements secondLineElements = secondLine.select("a[href*=item]");
        if (secondLineElements.size() > 0) {
            String commentsLine = secondLineElements.get(1).text().replaceAll("comments", "").replaceAll("comment", "");
            commentsLine = commentsLine.trim();
            if (commentsLine.equals("discuss")){
                //no comments yet
                comments = 0;
            } else {
                try{
                    comments = Integer.valueOf(commentsLine);
                } catch (Exception e){
                    comments = 0;
                }
            }
        }
        return comments;

    }

    public static int parseItemId(Element secondLine) {
        int item_id;

        String item_i = secondLine.select("a[href*=item]").attr("href");
        String item_txt = item_i.replace("item?id=", "");
        if (item_txt.equals("")) {
            return 0;
        }
        item_id = Integer.valueOf(item_txt);

        return item_id;
    }

    public String parseNextPageUrl() {
        String nextUrl = document.select(".title:not(span[class=comhead])>a[href]:contains(more)").last().attr("href");
        nextUrl = Story.NEXT_URL_BASE + nextUrl;
        return nextUrl;
    }
}
