package com.malmstein.yahnac.model;

public class Login {

    public enum Status{
        SUCCESSFUL,
        WRONG_CREDENTIALS,
        NETWORK_ERROR
    }
    private final String username;
    private final String cookie;
    private final Status successful;

    public Login(String username, String cookie, Status successful) {
        this.username = username;
        this.cookie = cookie;
        this.successful = successful;
    }

    public String getUsername() {
        return username;
    }

    public String getCookie() {
        return cookie;
    }

    public Status getStatus() {
        return successful;
    }
}
