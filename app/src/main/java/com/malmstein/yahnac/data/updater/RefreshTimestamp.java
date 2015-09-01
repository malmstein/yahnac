package com.malmstein.yahnac.data.updater;

public final class RefreshTimestamp {

    private final long millis;

    private RefreshTimestamp(long millis) {
        this.millis = millis;
    }

    public static RefreshTimestamp from(long millis) {
        return new RefreshTimestamp(millis);
    }

    public static RefreshTimestamp now() {
        return new RefreshTimestamp(System.currentTimeMillis());
    }

    public long getMillis() {
        return millis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RefreshTimestamp sunTimestamp = (RefreshTimestamp) o;

        return millis == sunTimestamp.millis;
    }

    @Override
    public int hashCode() {
        return (int) (millis ^ (millis >>> 32));
    }

}
