package com.malmstein.yahnac.tasks;

import com.malmstein.yahnac.model.Login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoginTask {

    static String LOGIN_PAGE_URL = "https://news.ycombinator.com/login";
    static String LOGIN_URL = "https://news.ycombinator.com/x";

    private final String username;
    private final String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Login execute() throws IOException {

        try {
            Document loginpage = Jsoup.connect(LOGIN_PAGE_URL).get();
            String fnid = loginpage.select("input[name=fnid]").attr("value");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(LOGIN_URL);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("fnid", fnid));
            nameValuePairs.add(new BasicNameValuePair("u", username));
            nameValuePairs.add(new BasicNameValuePair("p", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httppost.addHeader("Accept", "text/plain");
//            httppost.addHeader("User-agent", "Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

            HttpResponse response = httpclient.execute(httppost);
            Header[] cookies = response.getHeaders("Set-Cookie");
            if (cookies.length > 1) {
                String cookie = null;
                for (int i = 0; i < cookies.length; i++) {
                    cookie += cookies[i].getValue() + ';';
                }
                return new Login(username, cookie, Login.Status.SUCCESSFUL);
            } else {
                return new Login(username, null, Login.Status.WRONG_CREDENTIALS);
            }
        } catch (Exception e) {
            return new Login(username, null, Login.Status.NETWORK_ERROR);
        }

    }
}
