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
    private static ZoneOffset defaultZoneOffset = dateTime.getOffset();

    public static OffsetDateTime getDateTime() {
        return dateTime;
    }

    public static void setDateTime(OffsetDateTime dateTime) {
        DateTimeHelper.dateTime = dateTime;
    }

    public static OffsetDateTime offsetTime(ZoneOffset zoneOffset) {
        return dateTime.withOffsetSameInstant(zoneOffset);
    }

    public static void changeTimeZone(ZoneOffset zoneOffset) {
        dateTime = offsetTime(zoneOffset);
    }

    public static void resetTimeZone() {
        dateTime = dateTime.withOffsetSameInstant(defaultZoneOffset);
    }

    public static String dateTime(String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String dateTime(LocalDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String dateTime(OffsetDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String dateTime(ZonedDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String utcTime() {
        return offsetTime(ZoneOffset.UTC).toString();
    }

    public static String utcTime(String format) {
        return offsetTime(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(format));
    }

    public static String currentUtcTime() {
        return OffsetDateTime.now(ZoneOffset.UTC).toString();
    }

    public static String currentUtcTime(String format) {
        return OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(format));
    }

    public static String currentTime() {
        return dateTime(OffsetDateTime.now(), HH_MM_SS);
    }

    public static String currentDate() {
        return dateTime(OffsetDateTime.now(), YYYY_MM_DD);
    }

    public static String currentDateTime() {
        return dateTime(OffsetDateTime.now(), YYYY_MM_DD_HH_MM_SS);
    }

    public static String currentDateTime(String format) {
        return dateTime(OffsetDateTime.now(), format);
    }

    public static OffsetDateTime parseTime(String dateTime) {
        return OffsetDateTime.parse(dateTime);
    }

    public static OffsetDateTime parseTime(int year, int month, int day, int hour, int minute, int second, int nanoSecond, ZoneOffset zoneOffset) {
        return OffsetDateTime.of(year, month, day, hour, minute, second, nanoSecond, zoneOffset);
    }

    public static OffsetDateTime parseTime(int year, int month, int day, int hour, int minute, int second, int nanoSecond) {
        return parseTime(year, month, day, hour, minute, second, nanoSecond, defaultZoneOffset);
    }

    public static OffsetDateTime parseTime(int year, int month, int day, int hour, int minute, int second) {
        return parseTime(year, month, day, hour, minute, second, 0, defaultZoneOffset);
    }

    public static OffsetDateTime parseTime(int year, int month, int day, int hour, int minute) {
        return parseTime(year, month, day, hour, minute, 0, 0, defaultZoneOffset);
    }

    public static OffsetDateTime parseTime(int year, int month, int day, int hour) {
        return parseTime(year, month, day, hour, 0, 0, 0, defaultZoneOffset);
    }

    public static OffsetDateTime parseTime(int year, int month, int day) {
        return parseTime(year, month, day, 0, 0, 0, 0, defaultZoneOffset);
    }

    public static long nanos(OffsetDateTime dateTime) {
        return dateTime.toInstant().getEpochSecond();
    }

    public static long nanos(LocalDateTime dateTime) {
        return dateTime.toInstant(defaultZoneOffset).getEpochSecond();
    }

    public static long nanos(ZonedDateTime dateTime) {
        return dateTime.toInstant().getEpochSecond();
    }

    public static LocalDateTime convertTime(OffsetDateTime dateTime) {
        return dateTime.toLocalDateTime();
    }

    public static OffsetDateTime convertTime(LocalDateTime dateTime) {
        return OffsetDateTime.of(dateTime, defaultZoneOffset);
    }

    public static OffsetDateTime convertTime(ZonedDateTime dateTime) {
        return dateTime.toOffsetDateTime();
    }
}
