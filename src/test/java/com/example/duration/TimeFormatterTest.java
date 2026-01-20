package com.example.duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TimeFormatter")
class TimeFormatterTest {

    @Nested
    @DisplayName("Kata examples")
    class KataExamples {

        @ParameterizedTest(name = "{0} seconds = \"{1}\"")
        @CsvSource({
                "0, now",
                "1, 1 second",
                "62, 1 minute and 2 seconds",
                "120, 2 minutes",
                "3662, '1 hour, 1 minute and 2 seconds'",
                "15731080, '182 days, 1 hour, 44 minutes and 40 seconds'",
                "132030240, '4 years, 68 days, 3 hours and 4 minutes'"
        })
        void shouldFormatDuration(int seconds, String expected) {
            assertEquals(expected, TimeFormatter.formatDuration(seconds));
        }
    }

    @Nested
    @DisplayName("Singular units")
    class SingularUnits {

        @ParameterizedTest(name = "exactly 1 {1}")
        @CsvSource({
                "1, second",
                "60, minute",
                "3600, hour",
                "86400, day",
                "31536000, year"
        })
        void shouldUseSingularForm(int seconds, String unit) {
            assertEquals("1 " + unit, TimeFormatter.formatDuration(seconds));
        }
    }

    @Nested
    @DisplayName("Plural units")
    class PluralUnits {

        @ParameterizedTest(name = "2 {1}s")
        @CsvSource({
                "2, second",
                "120, minute",
                "7200, hour",
                "172800, day",
                "63072000, year"
        })
        void shouldUsePluralForm(int seconds, String unit) {
            assertEquals("2 " + unit + "s", TimeFormatter.formatDuration(seconds));
        }
    }

    @Nested
    @DisplayName("Boundary values")
    class BoundaryValues {

        @Test
        @DisplayName("59 seconds stays as seconds")
        void fiftyNineSecondsStaysAsSeconds() {
            assertEquals("59 seconds", TimeFormatter.formatDuration(59));
        }

        @Test
        @DisplayName("60 seconds becomes 1 minute")
        void sixtySecondsBecomesOneMinute() {
            assertEquals("1 minute", TimeFormatter.formatDuration(60));
        }

        @Test
        @DisplayName("61 seconds becomes 1 minute and 1 second")
        void sixtyOneSecondsBecomesOneMinuteAndOneSecond() {
            assertEquals("1 minute and 1 second", TimeFormatter.formatDuration(61));
        }

        @Test
        @DisplayName("3599 seconds is just under 1 hour")
        void justUnderOneHour() {
            assertEquals("59 minutes and 59 seconds", TimeFormatter.formatDuration(3599));
        }

        @Test
        @DisplayName("3601 seconds is 1 hour and 1 second")
        void oneHourAndOneSecond() {
            assertEquals("1 hour and 1 second", TimeFormatter.formatDuration(3601));
        }
    }

    @Nested
    @DisplayName("All units combined")
    class AllUnitsCombined {

        @Test
        @DisplayName("all units singular")
        void allUnitsSingular() {
            int total = 31536000 + 86400 + 3600 + 60 + 1; // 1 of each
            assertEquals("1 year, 1 day, 1 hour, 1 minute and 1 second",
                    TimeFormatter.formatDuration(total));
        }

        @Test
        @DisplayName("all units plural")
        void allUnitsPlural() {
            int total = 2 * (31536000 + 86400 + 3600 + 60 + 1); // 2 of each
            assertEquals("2 years, 2 days, 2 hours, 2 minutes and 2 seconds",
                    TimeFormatter.formatDuration(total));
        }
    }

    @Nested
    @DisplayName("Input validation")
    class InputValidation {

        @Test
        @DisplayName("negative input throws IllegalArgumentException")
        void negativeInputThrows() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> TimeFormatter.formatDuration(-1)
            );
            assertEquals("Duration cannot be negative: -1", exception.getMessage());
        }

        @Test
        @DisplayName("large negative throws")
        void largeNegativeThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> TimeFormatter.formatDuration(Integer.MIN_VALUE));
        }
    }
}
