package com.malmstein.hnews.feed;

import static com.malmstein.hnews.feed.NewsUpdateEvent.Type.REFRESH_FINISHED;

public class NewsUpdateEventCachedOnSubscribe extends CachedOnSubscribe<NewsUpdateEvent> {

    public NewsUpdateEventCachedOnSubscribe() {
        super();
    }

    public NewsUpdateEventCachedOnSubscribe(NewsUpdateEvent defaultValue) {
        super(defaultValue);
    }

    @Override
    protected NewsUpdateEvent getRecoveryValue(Throwable e) {
        return new NewsUpdateEvent(REFRESH_FINISHED);
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
