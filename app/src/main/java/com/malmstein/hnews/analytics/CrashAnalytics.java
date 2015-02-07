package com.malmstein.hnews.analytics;

import android.content.Context;

public interface CrashAnalytics {

    /**
     * Check if crashes analytics is active.
     */
    boolean isActive();

    /**
     * Begin tracking crashes for this context.
     *
     * @param context
     */
    void startTracking(Context context);

    /**
     * Send custom event to crash analyser service.
     *
     * @param errorMessage
     */
    void logSomethingWentWrong(String errorMessage);

    /**
     * Send exception with a message explaining the context.
     *
     * @param errorMessage
     * @param throwable
     */
    void logSomethingWentWrong(String errorMessage, Throwable throwable);

}
