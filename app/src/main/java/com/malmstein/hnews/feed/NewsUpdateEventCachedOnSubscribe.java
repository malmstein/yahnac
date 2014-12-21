package com.malmstein.hnews.feed;

import static com.malmstein.hnews.feed.StoriesUpdateEvent.Type.REFRESH_FINISHED;

public class NewsUpdateEventCachedOnSubscribe extends CachedOnSubscribe<StoriesUpdateEvent> {

    public NewsUpdateEventCachedOnSubscribe() {
        super();
    }

    public NewsUpdateEventCachedOnSubscribe(StoriesUpdateEvent defaultValue) {
        super(defaultValue);
    }

    @Override
    protected StoriesUpdateEvent getRecoveryValue(Throwable e) {
        return new StoriesUpdateEvent(REFRESH_FINISHED);
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
