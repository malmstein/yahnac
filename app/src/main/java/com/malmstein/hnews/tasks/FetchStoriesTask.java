package com.malmstein.hnews.tasks;

import android.content.ContentValues;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.stories.StoriesParser;

import java.io.IOException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchStoriesTask {

    private final Story.TYPE type;
    private final String url;

    public FetchStoriesTask(Story.TYPE type) {
        this.type = type;
        switch (type) {
            case top_story:
                url = "https://news.ycombinator.com/news";
                break;
            case new_story:
                url = "https://news.ycombinator.com/newest";
                break;
            case best_story:
                url = "https://news.ycombinator.com/best";
                break;
            case show:
                url = "https://news.ycombinator.com/show";
                break;
            case ask:
                url = "https://news.ycombinator.com/ask";
                break;
            default:
                throw new DeveloperError("Story type not recognised");
        }

    }

    public Vector<ContentValues> execute() throws IOException {

        Vector<ContentValues> stories;

        //Here we'll add the cookie when fetched
        Document doc = Jsoup.connect(url)
                .userAgent(System.getProperty("http.agent"))
                .header("Accept-Encoding", "gzip")
                .followRedirects(true)
                .post();

        stories = new StoriesParser(doc).parse(type);

        return stories;
    }

}
