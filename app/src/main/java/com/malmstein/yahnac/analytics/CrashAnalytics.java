package com.malmstein.yahnac.analytics;

public interface CrashAnalytics {

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
