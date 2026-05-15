package com.example.rakta_seva.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeAgoFormatter {

    /**
     * Formats a timestamp (millis) to a relative time string.
     */
    public static String format(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        if (diff < 0) {
            return "Just now";
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " mins ago";
        } else if (hours < 24) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (days == 1) {
            return "Yesterday";
        } else if (days < 7) {
            return days + " days ago";
        } else if (days < 30) {
            long weeks = days / 7;
            return weeks + (weeks == 1 ? " week ago" : " weeks ago");
        } else if (days < 365) {
            long months = days / 30;
            return months + (months == 1 ? " month ago" : " months ago");
        } else {
            long years = days / 365;
            return years + (years == 1 ? " year ago" : " years ago");
        }
    }

    /**
     * Calculates months ago from a date string (format: "MMMM dd, yyyy" or "MM/dd/yyyy").
     */
    public static String monthsAgoFromDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }

        SimpleDateFormat[] formats = {
                new SimpleDateFormat("MMMM dd, yyyy", Locale.US),
                new SimpleDateFormat("MMMM d, yyyy", Locale.US),
                new SimpleDateFormat("MM/dd/yyyy", Locale.US),
                new SimpleDateFormat("yyyy-MM-dd", Locale.US)
        };

        Date date = null;
        for (SimpleDateFormat format : formats) {
            try {
                date = format.parse(dateStr);
                if (date != null) break;
            } catch (ParseException ignored) {
            }
        }

        if (date == null) {
            return "";
        }

        long diff = System.currentTimeMillis() - date.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (days < 30) {
            return days + " days ago";
        } else {
            long months = days / 30;
            return months + (months == 1 ? " month ago" : " months ago");
        }
    }
}
