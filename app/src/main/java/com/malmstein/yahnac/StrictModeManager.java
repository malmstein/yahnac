package com.malmstein.yahnac;

import android.os.StrictMode;

/**
 * Enables StrictMode with defaults to detect all wrong doing.
 * <p/>
 * See http://developer.android.com/reference/android/os/StrictMode.html for more
 */
public final class StrictModeManager {

    private StrictModeManager() {
        throw new IllegalStateException("This class should not be instantiated");
    }

    /**
     * Initializes StrictMode to close activity and log to console when a violation occurs.
     */
    public static void initializeStrictMode() {
        initializeStrictMode(newVmPolicyBuilderWithDefaults(), newThreadPolicyBuilderWithDefaults());
    }

    public static void initializeStrictMode(StrictMode.VmPolicy.Builder vmPolicyBuilder, StrictMode.ThreadPolicy.Builder threadPolicyBuilder) {
        StrictMode.setThreadPolicy(threadPolicyBuilder.penaltyDeath().build());
        StrictMode.setVmPolicy(vmPolicyBuilder.penaltyDeath().build());
    }

    /**
     * Initializes StrictMode to log to console when a violation occurs.
     */
    public static void initializeStrictModeLogOnly() {
        initializeStrictModeLogOnly(newVmPolicyBuilderWithDefaults(), newThreadPolicyBuilderWithDefaults());
    }

    public static void initializeStrictModeLogOnly(StrictMode.VmPolicy.Builder vmPolicyBuilder, StrictMode.ThreadPolicy.Builder threadPolicyBuilder) {
        StrictMode.setThreadPolicy(threadPolicyBuilder.build());
        StrictMode.setVmPolicy(vmPolicyBuilder.build());
    }

    public static StrictMode.VmPolicy.Builder newVmPolicyBuilderWithDefaults() {
        return new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();
    }

    public static StrictMode.ThreadPolicy.Builder newThreadPolicyBuilderWithDefaults() {
        return new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
    }

}
