package com.malmstein.hnews.comments;

import com.malmstein.hnews.feed.CachedOnSubscribe;

import static com.malmstein.hnews.comments.CommentsUpdateEvent.Type.REFRESH_FINISHED;

public class CommentsUpdateEventCachedOnSubscribe extends CachedOnSubscribe<CommentsUpdateEvent> {

    public CommentsUpdateEventCachedOnSubscribe() {
        super();
    }

    public CommentsUpdateEventCachedOnSubscribe(CommentsUpdateEvent defaultValue) {
        super(defaultValue);
    }

    @Override
    protected CommentsUpdateEvent getRecoveryValue(Throwable e) {
        return new CommentsUpdateEvent(REFRESH_FINISHED);
    }

    /**
     * we explicitly don't forward onCompleted() because we want the subscribers
     * to listen to an infinite stream.
     */
    @Override
    public void onCompleted() {
        // no op
    }

}
