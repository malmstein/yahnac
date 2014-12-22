package com.malmstein.hnews.comments;

import android.content.ContentValues;
import android.text.Html;
import android.text.Spanned;

import com.malmstein.hnews.data.HNewsContract;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class CommentsParser {

    private final Long storyId;
    private final Document document;

    private int level;
    static Vector<ContentValues> commentsList = new Vector<>();

    public CommentsParser(Long storyId, Document document) {
        this.storyId = storyId;
        this.document = document;
    }

    public Vector<ContentValues> parse() {
        commentsList.clear();
        Elements comments = document.select("body>center>table>tbody>tr>td>table>tbody>tr>td>table>tbody>tr");

        for (Element comment : comments) {
            String submitter = comment.select("span[class=comhead]>a").get(0).text();
            String text = comment.select("span[class=comment]").html();
            String nested = comment.select("img[src=s.gif]").attr("width");

            String time = comment.select("span[class=comhead]").text().replaceAll(submitter, "").replaceAll("\\|", "").replaceAll("link", "");
            String commentid = comment.select("span[class=comhead]>a").get(1).attr("href");

            //extract part between <pre> and </pre>
            if (text.contains("<pre>")) {
                String temp_text = text;

                while (temp_text.contains("<pre>")) {
                    Integer pos_start = temp_text.indexOf("<pre>");
                    Integer pos_end = temp_text.indexOf("</pre>") + 5; // 5 for length of </pre>

                    String code = temp_text.substring(pos_start, pos_end);
                    code = code.replaceAll("\\r\\n|\\r|\\n", "<br>");
                    code = code.replaceAll("  ", "&nbsp;&nbsp;");
                    code = code.replace("<pre>", "");
                    code = code.replace("</pre>", "");
                    code = code.replace("<code>", "<blockquote><tt>");
                    code = code.replace("</code>", "</tt></blockquote>");

                    temp_text = temp_text.substring(0, pos_start) + code + temp_text.substring(pos_end);
                }
                text = temp_text;
            }

            if (text.contains("<p>") && text.contains("reply?id=")) { // removed reply link from multi paragraph replies
                text = text.substring(0, text.lastIndexOf("<p>"));
            } else {
                text = text + "<p></p>"; // maintains uniformity with multiparagraph lines
            }

            text = Jsoup.clean(text, Whitelist.basic());
            Spanned text_sp = Html.fromHtml(text);

            Elements a_links = Jsoup.parse(text).select("a[href]");

            //extract all links in the reply
            String[] url_list = new String[a_links.size()];
            int i = 0;
            for (Element a : a_links) {
                url_list[i++] = a.attr("href");
            }

            if (nested.equals("0")) {
                level = 0;
            } else if (nested.equals("40")) {
                level = 15;
            } else if (nested.equals("80")) {
                level = 30;
            } else if (nested.equals("120")) {
                level = 45;
            } else if (nested.equals("160")) {
                level = 60;
            } else if (nested.equals("200")) {
                level = 75;
            } else if (nested.equals("240")) {
                level = 90;
            } else if (nested.equals("280")) {
                level = 105;
            } else if (nested.equals("320")) {
                level = 120;
            } else if (nested.equals("360")) {
                level = 135;
            } else {
                level = 0;
            }

            ContentValues commentValues = new ContentValues();

            commentValues.put(HNewsContract.ItemEntry.COLUMN_BY, submitter);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_ITEM_ID, storyId);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_TEXT, text);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_LEVEL, level);
            commentValues.put(HNewsContract.ItemEntry.COLUMN_TIME_TEXT, time);

            commentsList.add(commentValues);
        }

        return commentsList;
    }
}
