package ru.bootdev.test.core.helper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {

    public static final String HH_MM = "HH:mm";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static OffsetDateTime dateTime = OffsetDateTime.now();
    private static final ZoneOffset defaultZoneOffset = dateTime.getOffset();

    public static OffsetDateTime getDateTime() {
        return dateTime;
    }

    public static void setDateTime(OffsetDateTime dateTime) {
        DateTimeHelper.dateTime = dateTime;
    }

    public static OffsetDateTime getOffsetTime(ZoneOffset zoneOffset) {
        return dateTime.withOffsetSameInstant(zoneOffset);
    }

    public static void setZoneOffset(ZoneOffset zoneOffset) {
        dateTime = getOffsetTime(zoneOffset);
    }

    public static void resetZoneOffset() {
        dateTime = dateTime.withOffsetSameInstant(defaultZoneOffset);
    }

    public static String getFormattedDateTime(String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String getFormattedDateTime(LocalDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String getFormattedDateTime(OffsetDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String getFormattedDateTime(ZonedDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String getUtcTime() {
        return getOffsetTime(ZoneOffset.UTC).toString();
    }

    public static String getCurrentTime() {
        return getFormattedDateTime(OffsetDateTime.now(), HH_MM_SS);
    }

    public static String getCurrentDate() {
        return getFormattedDateTime(OffsetDateTime.now(), YYYY_MM_DD);
    }

    public static String getCurrentDateTime() {
        return getFormattedDateTime(OffsetDateTime.now(), YYYY_MM_DD_HH_MM_SS);
    }

    public static String getCurrentDateTime(String format) {
        return getFormattedDateTime(OffsetDateTime.now(), format);
    }
}
