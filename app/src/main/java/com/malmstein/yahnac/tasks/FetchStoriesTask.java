package com.malmstein.yahnac.tasks;

import android.text.TextUtils;

import com.malmstein.yahnac.base.DeveloperError;
import com.malmstein.yahnac.model.StoriesJsoup;
import com.malmstein.yahnac.model.Story;
import com.malmstein.yahnac.stories.StoriesParser;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchStoriesTask {

    private final Story.TYPE type;
    private final String url;

    public FetchStoriesTask(Story.TYPE type, String nextUrl) {
        this.type = type;
        if (TextUtils.isEmpty(nextUrl)) {
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
        } else {
            url = nextUrl;
        }

    }

    public StoriesJsoup execute() throws IOException {

        //Here we'll add the cookie when fetched
        Document doc = Jsoup.connect(url)
                .userAgent(System.getProperty("http.agent"))
                .header("Accept-Encoding", "gzip")
                .followRedirects(true)
                .post();

        return new StoriesParser(doc).parse(type);
    }

}
