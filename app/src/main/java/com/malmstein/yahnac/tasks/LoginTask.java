package com.malmstein.yahnac.tasks;

import android.text.TextUtils;

import com.malmstein.yahnac.model.Login;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class LoginTask {

    public static final String BASE_URL = "https://news.ycombinator.com";
    public static final String USER_AGENT = System.getProperty("http.agent");
    public static final int TIMEOUT_MILLIS = 40 * 1000;
    private static final String LOGIN_URL_EXTENSION = "/login?go_to=news";
    private final String username;
    private final String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Connection anonConnect(String baseUrlExtension) {
        Connection conn = Jsoup.connect(BASE_URL + baseUrlExtension)
                .timeout(TIMEOUT_MILLIS)
                .userAgent(USER_AGENT);
        conn.header("Accept-Encoding", "gzip");

        return conn;
    }

    public Login execute() throws IOException {

        Connection.Response response = anonConnect("/login")
                .data("go_to", "news")
                .data("acct", username)
                .data("pw", password)
                .header("Origin", BASE_URL)
                .followRedirects(true)
                .referrer(BASE_URL + LOGIN_URL_EXTENSION)
                .method(Connection.Method.POST)
                .execute();

        String cookie = response.cookie("user");

        if (!TextUtils.isEmpty(cookie)) {
            return new Login(username, cookie, Login.Status.SUCCESSFUL);
        } else {
            return new Login(username, null, Login.Status.WRONG_CREDENTIALS);
        }
    }
}
