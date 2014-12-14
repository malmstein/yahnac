package com.malmstein.hnews.feed;

public class NewsUpdateEvent {

    public enum Type {
        REFRESH_STARTED,
        REFRESH_FINISHED;
    }

    private final Type type;

    public NewsUpdateEvent(Type type) {
        this.type = type;
    }

    public boolean isRefreshStarted() {
        return type == Type.REFRESH_STARTED;
    }

    public boolean isRefreshFinished() {
        return type == Type.REFRESH_FINISHED;
    }

}
