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

            Element el = subtext.get(i);
            String point_i = subtext.get(i).select("td[class=subtext]>span").text().replaceAll(" points", "").replaceAll(" point", "");
            String title_i = titles.get(i).select("td[class=title]>a").text();
            String domain = titles.get(i).select("td[class=title]>span").text();
            String article_url_i = titles.get(i).select("td[class=title]>a").attr("href");
            String submitter_i = subtext.get(i).select("a[href*=user]").text();
            String comments_i = subtext.get(i).select("a[href*=item]").text().replaceAll("discuss", "0 comments");

            int item_id = 0;
            try{
                String item_i = subtext.get(i).select("a[href*=item]").attr("href");
                String item_txt = item_i.replace("item?id=", "");
                item_id = Integer.valueOf(item_txt);
            } catch (Exception e){
                item_id = 0;
            }


            String upvote_i = titles.get(i).select("td>center>a[href^=vote]").attr("href");
            i++;

            ContentValues storyValues = new ContentValues();

            storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, item_id);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TYPE, type.name());
            storyValues.put(HNewsContract.ItemEntry.COLUMN_BY, submitter_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TIME, submitter_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_COMMENTS, comments_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_DOMAIN, domain);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_URL, article_url_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_SCORE, point_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_TITLE, title_i);
            storyValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ORDER, i);

            storiesList.add(storyValues);
        }

        return storiesList;
    }



}
