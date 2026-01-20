package com.example.duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for formatting durations in a human-readable format.
 */
public final class TimeFormatter {

    private static final String NOW = "now";
    private static final String SEPARATOR = ", ";
    private static final String LAST_SEPARATOR = " and ";

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = 60 * SECONDS_PER_MINUTE;
    private static final int SECONDS_PER_DAY = 24 * SECONDS_PER_HOUR;
    private static final int SECONDS_PER_YEAR = 365 * SECONDS_PER_DAY;

    private enum TimeUnit {
        YEAR(SECONDS_PER_YEAR, "year"),
        DAY(SECONDS_PER_DAY, "day"),
        HOUR(SECONDS_PER_HOUR, "hour"),
        MINUTE(SECONDS_PER_MINUTE, "minute"),
        SECOND(1, "second");

        private final int seconds;
        private final String singular;

        TimeUnit(int seconds, String singular) {
            this.seconds = seconds;
            this.singular = singular;
        }

        String format(int count) {
            return count == 1
                    ? count + " " + singular
                    : count + " " + singular + "s";
        }
    }

    private TimeFormatter() {
        // Utility class - prevent instantiation
    }

    /**
     * Formats a duration given in seconds into a human-readable string.
     *
     * <p>Examples:
     * <ul>
     *   <li>{@code formatDuration(0)} returns {@code "now"}</li>
     *   <li>{@code formatDuration(62)} returns {@code "1 minute and 2 seconds"}</li>
     *   <li>{@code formatDuration(3662)} returns {@code "1 hour, 1 minute and 2 seconds"}</li>
     * </ul>
     *
     * @param seconds the duration in seconds (must be non-negative)
     * @return human-readable duration string
     * @throws IllegalArgumentException if seconds is negative
     */
    public static String formatDuration(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Duration cannot be negative: " + seconds);
        }

        if (seconds == 0) {
            return NOW;
        }

        List<String> parts = extractTimeParts(seconds);
        return joinWithCommasAndAnd(parts);
    }

    private static List<String> extractTimeParts(int totalSeconds) {
        List<String> parts = new ArrayList<>();
        int remaining = totalSeconds;

        for (TimeUnit unit : TimeUnit.values()) {
            int count = remaining / unit.seconds;
            if (count > 0) {
                parts.add(unit.format(count));
                remaining %= unit.seconds;
            }
        }

        return parts;
    }

    private static String joinWithCommasAndAnd(List<String> parts) {
        int size = parts.size();

        if (size == 1) {
            return parts.getFirst();
        }

        String allButLast = String.join(SEPARATOR, parts.subList(0, size - 1));
        String last = parts.getLast();

        return allButLast + LAST_SEPARATOR + last;
    }
}
