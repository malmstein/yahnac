package com.malmstein.yahnac.model;

import android.content.res.Resources;

import com.malmstein.yahnac.R;

import java.util.Date;

public class TimeAgo {

    private final Resources resources;

    public TimeAgo(Resources resources) {
        this.resources = resources;
    }

    /**
     * Get time ago that date occurred
     *
     * @param date
     * @return time string
     */
    public String timeAgo(final Date date) {
        return timeAgo(date.getTime());
    }

    /**
     * Get time ago that milliseconds date occurred
     *
     * @param millis
     * @return time string
     */
    public String timeAgo(final long millis) {
        return time(System.currentTimeMillis() - millis);
    }

    /**
     * Get time string for milliseconds distance
     *
     * @param distanceMillis
     * @return time string
     */

    public String time(Long distanceMillis) {

        final double seconds = distanceMillis / 1000;
        final double minutes = seconds / 60;
        final double hours = minutes / 60;
        final double days = hours / 24;
        final double years = days / 365;

        final String time;
        if (seconds < 45) {
            time = resources.getString(R.string.time_seconds);
        } else if (seconds < 90 || minutes < 45) {
            time = resources.getQuantityString(R.plurals.time_minute, minutes < 2 ? 1 : 2, Math.round(minutes));
        } else if (minutes < 90 || hours < 24) {
            time = resources.getQuantityString(R.plurals.time_hour, hours < 2 ? 1 : 2, Math.round(hours));
        } else if (hours < 48 || days < 30) {
            time = resources.getQuantityString(R.plurals.time_day, days < 2 ? 1 : 2, Math.round(days));
        } else if (days < 60 || days < 365) {
            time = resources.getQuantityString(R.plurals.time_month, (days / 30) < 2 ? 1 : 2, Math.round(days / 30));
        } else {
            time = resources.getQuantityString(R.plurals.time_year, years < 2 ? 1 : 2, Math.round(years));
        }

        return time + " " + resources.getString(R.string.time_ago);

    }

}

