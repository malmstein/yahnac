package com.malmstein.yahnac.model;

import java.util.Calendar;
import java.util.Date;

public class HNewsDate {

    private static final int MILLIS_IN_A_SEC = 1000;
    private static final int TWO_DAYS = 2;
    private static final int SEC_IN_A_MIN = 60;
    private static final int MIN_IN_AN_HOUR = 60;
    private static final int HOUR_IN_A_DAY = 24;
    private static final int MILLIS_IN_A_MIN = HNewsDate.MILLIS_IN_A_SEC * SEC_IN_A_MIN;
    private static final int MILLIS_IN_AN_HOUR = MILLIS_IN_A_MIN * MIN_IN_AN_HOUR;
    private static final int MILLIS_IN_A_DAY = MILLIS_IN_AN_HOUR * HOUR_IN_A_DAY;
    private static final int MILLIS_IN_TWO_DAYS = MILLIS_IN_A_DAY * TWO_DAYS;

    private final Calendar calendar;

    public HNewsDate(Calendar calendar) {
        this.calendar = calendar;
    }

    public static HNewsDate now() {
        return from(new Date());
    }

    public static HNewsDate from(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new HNewsDate(calendar);
    }

    public HNewsDate twoDaysAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.calendar.getTimeInMillis() - MILLIS_IN_TWO_DAYS);
        return new HNewsDate(calendar);
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

}
