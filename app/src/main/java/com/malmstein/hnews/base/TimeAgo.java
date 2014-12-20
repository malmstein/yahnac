package com.malmstein.hnews.base;

import android.content.res.Resources;

import com.malmstein.hnews.R;

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
	public String time(long distanceMillis) {

		final int seconds = (int) distanceMillis / 1000;
		final int minutes = seconds / 60;
		final int hours = minutes / 60;
		final int days = hours / 24;
		final int years = days / 365;

		final String time;
		if (seconds < 45)
			time = resources.getString(R.string.time_seconds);
		else if (seconds < 90 || minutes < 45)
			time = resources.getQuantityString(R.plurals.time_minute, minutes, minutes);
		else if (minutes < 90 || hours < 24)
            time = resources.getQuantityString(R.plurals.time_hour, hours, hours);
		else if (hours < 48 || days < 30)
            time = resources.getQuantityString(R.plurals.time_day, days, days);
		else if (days < 60 || days < 365)
            time = resources.getQuantityString(R.plurals.time_month, days / 30, days / 30);
		else
            time = resources.getQuantityString(R.plurals.time_year, years, years);

        return time + " " + resources.getString(R.string.time_ago);

	}

}