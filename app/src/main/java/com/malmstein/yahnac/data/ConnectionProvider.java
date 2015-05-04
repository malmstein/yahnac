package com.malmstein.yahnac.data;

import com.malmstein.yahnac.updater.LoginSharedPreferences;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class ConnectionProvider {

    public static final String BASE_URL = "https://news.ycombinator.com";

    public static final String LOGIN_URL_EXTENSION = "/login?go_to=news";
    public static final String LOGIN_BASE_URL = "/login";

    public static final String USER_AGENT = System.getProperty("http.agent");
    public static final int TIMEOUT_MILLIS = 40 * 1000;

    private final LoginSharedPreferences loginSharedPreferences;

    public ConnectionProvider() {
        this.loginSharedPreferences = LoginSharedPreferences.newInstance().newInstance();
    }

    private static Connection defaultConnection(String baseUrlExtension) {
        Connection conn = Jsoup.connect(BASE_URL + baseUrlExtension)
                .timeout(TIMEOUT_MILLIS)
                .userAgent(USER_AGENT);
        conn.header("Accept-Encoding", "gzip");

        return conn;
    }

    private static Connection authorisedConnection(String baseUrlExtension, String userCookie) {
        return defaultConnection(baseUrlExtension).cookie("user", userCookie);
    }

    private Connection connection(String baseUrlExtension) {
        if (loginSharedPreferences.isLoggedIn()) {
            return defaultConnection(baseUrlExtension);
        } else {
            return authorisedConnection(baseUrlExtension, loginSharedPreferences.getCookie());
        }
    }

    public Connection loginConnection(String username, String password) {
        Connection login = connection(LOGIN_BASE_URL);
        return login
                .data("go_to", "news")
                .data("acct", username)
                .data("pw", password)
                .header("Origin", ConnectionProvider.BASE_URL)
                .followRedirects(true)
                .referrer(ConnectionProvider.BASE_URL + ConnectionProvider.LOGIN_URL_EXTENSION)
                .method(Connection.Method.POST);

    }
}

