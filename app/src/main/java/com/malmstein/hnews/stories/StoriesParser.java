package com.malmstein.hnews.stories;

import android.content.ContentValues;

import com.malmstein.hnews.data.HNewsContract;
import com.malmstein.hnews.model.Story;

import java.util.ListIterator;
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

    public Vector<ContentValues> parser(Story.TYPE type) {

        storiesList.clear();

        // check for expired fnid
        Element body = document.body();
        String bodyText = body.text();
        if (bodyText.equals("Unknown or expired link.")) {

            return storiesList;
        }

        Elements titles = document.select("td.title");
        Elements subtexts = document.select("td.subtext");

        ListIterator<Element> titlesIterator = titles.listIterator();
        ListIterator<Element> subtextIterator = subtexts.listIterator();

        while (titlesIterator.hasNext() && subtextIterator.hasNext()) {
            Element child = titlesIterator.next();
            Element titleElement = child.parent();
            Element subtextElement = subtextIterator.next();
            storiesList.add(parseStory(titleElement, subtextElement, false, type));
        }

        return storiesList;
    }

    public static ContentValues parseStory(Element title, Element subtext, boolean loggedIn, Story.TYPE type) {

        int storyPosition = parsePosition(title);
        long storyId;
        int storyPoints;
        int storyComments;
        String storyDomain = "";
        String storyUrl = "";
        String storyAgo = "";
        String storySubmitter;
        String potentialJobsUrl = "";
        String storyTitle = "";

        try {
            Element titleLink = title.select("td.title > a").first();
            storyTitle = titleLink.text();

            // try to get url & domain, if it fails you're on a self post
            try {
                storyUrl = titleLink.attr("href");
                // if url starts with item?id, it's a self post & may potentially be a url for a jobs post
                if (storyUrl.startsWith("item?id=")) {
                    potentialJobsUrl = "https://news.ycombinator.com/" + storyUrl;
                }
                storyDomain = parseDomain(title);
            } catch (NullPointerException e) {
                storyUrl = null;
                storyDomain = null;
            }

            storyId = parseStoryId(subtext);

            // if the user is logged in, get isUpvoted, whence, and auth
//            if (loggedIn) {
//
//                story.isUpvoted = true;
//                story.whence = null;
//                story.auth = null;
//
//                Element voteAnchor = title.select("a[href^=vote]")
//                        .first();
//
//                if (voteAnchor != null) {
//                    String[] voteHref = voteAnchor.attr("href")
//                            .split("[=&]");
//
//                    story.isUpvoted = false;
//                    story.whence = voteHref[voteHref.length - 1];
//                    story.auth = voteHref[7];
//                }
//            }

            storyPoints = parseNumPoints(subtext);
            storySubmitter = (subtext.select("a[href^=user]").text());
            storyAgo = parseAgo(subtext, storySubmitter);
            storyComments = parseNumComments(subtext);

        } catch (Exception e) {
            // this means it's a YCombinator jobs post
            storyId = 0;
//            story.whence = null;
            storyPoints = 0;
            storySubmitter = "JOBS";
            storyComments = 0;
            if (potentialJobsUrl != null) {
                storyUrl = potentialJobsUrl;
            }
        }

        ContentValues storyValues = new ContentValues();

        storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, storyId);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TYPE, type.name());
        storyValues.put(HNewsContract.ItemEntry.COLUMN_BY, storySubmitter);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TIME_AGO, storyAgo);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_COMMENTS, storyComments);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_DOMAIN, storyDomain);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_URL, storyUrl);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_SCORE, storyPoints);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TITLE, storyTitle);
        storyValues.put(HNewsContract.ItemEntry.COLUMN_TIMESTAMP, System.currentTimeMillis());

        return storyValues;
    }

    private static int parsePosition(Element title) {
        try {
            String position = title.child(0)
                    .text()
                    .replace(".", "");
            return Integer.parseInt(position);
        } catch (Exception e) { // TODO fix exception catch'em all!
            // this means we're on the comments page
            return -1;
        }
    }

    private static String parseDomain(Element title) {
        String domain = title.select("span.comhead")
                .first()
                .text()
                .trim();
        // trim parens from domain;
        domain = domain.substring(1, domain.length() - 1);
        return domain;
    }

//    private static String parseAgo(Element subtext) {
//        return ((TextNode) subtext.childNode(3)).text()
//                .replace("|", "")
//                .trim();
//    }

    private static String parseAgo(Element subtext, String submitter) {
        String whole = subtext.text();
        int start = whole.indexOf(submitter) + 1;
        int end = whole.indexOf("|");

        return whole.substring(start + submitter.length(), end);
    }

    private static long parseStoryId(Element subtext) {
        return Long.parseLong(subtext.select("a[href^=item]")
                .attr("href")
                .split("=")[1]);
    }

    private static int parseNumPoints(Element subtext) {
        return Integer.parseInt(subtext.select("span[id^=score]")
                .first()
                .text()
                .split("\\s")[0]);
    }

    private static int parseNumComments(Element subtext) {
        try {
            return (Integer.parseInt(subtext.select("a[href^=item]")
                    .first()
                    .text()
                    .split("\\s")[0]));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Vector<ContentValues> parse(Story.TYPE type) {
        storiesList.clear();

        Elements titles = document.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=title])");
        Elements subtext = document.select("body>center>table>tbody>tr>td>table>tbody>tr:has(td[class=subtext])");

        Integer i = 0;
        for (Element st : subtext) {

            String title_i = titles.get(i).select("td[class=title]>a").text();
            String domain = titles.get(i).select("td[class=title]>span").text();

            String point_i = subtext.get(i).select("td[class=subtext]>span").text().replaceAll(" points", "").replaceAll(" point", "");
            String article_url_i = titles.get(i).select("td[class=title]>a").attr("href");
            String submitter_i = subtext.get(i).select("a[href*=user]").text();
            String comments_i = subtext.get(i).select("a[href*=item]").text().replaceAll("discuss", "0 comments");

            int item_id = 0;
            try {
                String item_i = subtext.get(i).select("a[href*=item]").attr("href");
                String item_txt = item_i.replace("item?id=", "");
                item_id = Integer.valueOf(item_txt);
            } catch (Exception e) {
                item_id = 0;
            }

            String upvote_i = titles.get(i).select("td>center>a[href^=vote]").attr("href");
            String ago = parseAgo(subtext.get(i), submitter_i);

            i++;

            ContentValues storyValues = new ContentValues();

            storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, item_id);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TYPE, type.name());
            storyValues.put(HNewsContract.ItemEntry.COLUMN_BY, submitter_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TIME_AGO, submitter_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_COMMENTS, comments_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_DOMAIN, domain);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_URL, article_url_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_SCORE, point_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TITLE, title_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TIMESTAMP, System.currentTimeMillis());

            storiesList.add(storyValues);
        }

        return storiesList;
    }

}
