package com.malmstein.hnews.tasks;

import android.content.ContentValues;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.model.Story;
import com.malmstein.hnews.stories.StoriesParser;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchStoriesTask {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 1000;
    private static final int DEFAULT_READ_TIMEOUT = 15 * 1000;

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

        Vector<ContentValues> stories = new Vector<>();

        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(httpParameters, getReadTimeout());

        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpGet httpget = new HttpGet(url);

        HttpResponse response = httpclient.execute(httpget);
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {
            HttpEntity httpEntity = response.getEntity();

            String result = EntityUtils.toString(httpEntity);

            Document doc = Jsoup.parse(result);

            stories = new StoriesParser(doc).parse(type);
        }
        return stories;
    }

    protected int getConnectionTimeout() {
        return DEFAULT_CONNECTION_TIMEOUT;
    }

    protected int getReadTimeout() {
        return DEFAULT_READ_TIMEOUT;
    }
}
