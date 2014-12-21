package com.malmstein.hnews.comments;

public class CommentsUpdateEvent {

    public enum Type {
        REFRESH_STARTED,
        REFRESH_FINISHED;
    }

    private final Type type;

    public CommentsUpdateEvent(Type type) {
        this.type = type;
    }

    public boolean isRefreshStarted() {
        return type == Type.REFRESH_STARTED;
    }

    public boolean isRefreshFinished() {
        return type == Type.REFRESH_FINISHED;
    }

}
