package com.malmstein.hnews.base;

public class DeveloperError extends RuntimeException {

    public DeveloperError(String detailMessage) {
        super(detailMessage);
    }

    public DeveloperError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DeveloperError(Throwable throwable) {
        super(throwable);
    }

}
