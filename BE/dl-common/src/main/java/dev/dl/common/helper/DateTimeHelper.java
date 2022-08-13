package dev.dl.common.helper;


import dev.dl.common.constant.Constant;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@SuppressWarnings("ALL")
public class DateTimeHelper {

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert, String zone) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of(zone))
                .toLocalDate();
    }

    public static LocalDate convertToLocalDateViaMillisecond(Date dateToConvert, String zone) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.of(zone))
                .toLocalDate();
    }

    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert, String zone) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of(zone))
                .toLocalDateTime();
    }

    public static LocalDateTime convertToLocalDateTimeViaMillisecond(Date dateToConvert, String zone) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.of(zone))
                .toLocalDateTime();
    }

    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static Date convertToDateViaInstant(LocalDate dateToConvert, String zone) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.of(zone))
                .toInstant());
    }

    public static Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert, String zone) {
        return Date
                .from(dateToConvert.atZone(ZoneId.of(zone))
                        .toInstant());
    }

    public static long toUnix(LocalDateTime time, String... timeZone) {
        String zone = timeZone.length == 1 ? timeZone[0] : Constant.DEFAULT_TIMEZONE;
        return time.atZone(ZoneId.of(zone)).toEpochSecond();
    }

    public static LocalDateTime generateCurrentTimeBaseOnZoneId(String zoneId) {
        return ZonedDateTime.now(ZoneId.of(zoneId)).toLocalDateTime();
    }

    public static LocalDateTime generateCurrentTimeDefault() {
        return ZonedDateTime.now(ZoneId.of(Constant.DEFAULT_TIMEZONE)).toLocalDateTime();
    }

}
