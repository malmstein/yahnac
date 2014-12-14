package com.malmstein.hnews.feed;

import static com.malmstein.hnews.feed.FeedUpdateEvent.Type.REFRESH_FINISHED;

public class FeedUpdateEventCachedOnSubscribe extends CachedOnSubscribe<FeedUpdateEvent> {

    public FeedUpdateEventCachedOnSubscribe() {
        super();
    }

    public FeedUpdateEventCachedOnSubscribe(FeedUpdateEvent defaultValue) {
        super(defaultValue);
    }

    @Override
    protected FeedUpdateEvent getRecoveryValue(Throwable e) {
        return new FeedUpdateEvent(REFRESH_FINISHED);
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
