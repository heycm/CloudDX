package cn.heycm.common.tools.date;

import cn.heycm.common.entity.error.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具类
 *
 * @author heycm
 * @version 1.0
 * @since 2024/11/15 22:26
 */
public class DateUtil {

    private DateUtil() {}

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    private static final Map<String, DateTimeFormatter> FORMATTER = new ConcurrentHashMap<>();

    static {
        FORMATTER.put(DateFormat.YYYY_MM_DD, DateTimeFormatter.ofPattern(DateFormat.YYYY_MM_DD));
        FORMATTER.put(DateFormat.HH_MM_SS, DateTimeFormatter.ofPattern(DateFormat.HH_MM_SS));
        FORMATTER.put(DateFormat.YYYY_MM_DD_HH_MM_SS, DateTimeFormatter.ofPattern(DateFormat.YYYY_MM_DD_HH_MM_SS));
    }

    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        DateTimeFormatter formatter = FORMATTER.getOrDefault(pattern, DateTimeFormatter.ofPattern(pattern));
        Assert.notNull(formatter, "Invalid date format pattern");
        FORMATTER.putIfAbsent(pattern, formatter);
        return formatter;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZONE_ID).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay(ZONE_ID).toLocalDateTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZONE_ID).toLocalDate();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZONE_ID).toInstant());
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZONE_ID).toInstant());
    }

    public static String format(Date date, String pattern) {
        return toLocalDateTime(date).format(getDateTimeFormatter(pattern));
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(getDateTimeFormatter(pattern));
    }

    public static String format(LocalDate localDate, String pattern) {
        return localDate.format(getDateTimeFormatter(pattern));
    }

    public static Date toDate(String format, String pattern) {
        return toDate(toLocalDateTime(format, pattern));
    }

    public static LocalDate toLocalDate(String format, String pattern) {
        return LocalDate.parse(format, getDateTimeFormatter(pattern));
    }

    public static LocalDateTime toLocalDateTime(String format, String pattern) {
        return LocalDateTime.parse(format, getDateTimeFormatter(pattern));
    }

    public static LocalDateTime addSeconds(LocalDateTime localDateTime, int seconds) {
        if (seconds == 0) {
            return localDateTime;
        }
        return localDateTime.plusSeconds(seconds);
    }

    public static LocalDateTime addMinutes(LocalDateTime localDateTime, int minutes) {
        if (minutes == 0) {
            return localDateTime;
        }
        return localDateTime.plusMinutes(minutes);
    }

    public static LocalDateTime addHours(LocalDateTime localDateTime, int hours) {
        if (hours == 0) {
            return localDateTime;
        }
        return localDateTime.plusHours(hours);
    }

    public static LocalDateTime addDays(LocalDateTime localDateTime, int days) {
        if (days == 0) {
            return localDateTime;
        }
        return localDateTime.plusDays(days);
    }

    public static LocalDateTime addWeeks(LocalDateTime localDateTime, int weeks) {
        if (weeks == 0) {
            return localDateTime;
        }
        return localDateTime.plusWeeks(weeks);
    }

    public static LocalDateTime addMonths(LocalDateTime localDateTime, int months) {
        if (months == 0) {
            return localDateTime;
        }
        return localDateTime.plusMonths(months);
    }

    public static LocalDateTime addYears(LocalDateTime localDateTime, int years) {
        if (years == 0) {
            return localDateTime;
        }
        return localDateTime.plusYears(years);
    }


    public static Date addSeconds(Date date, int seconds) {
        return toDate(addSeconds(toLocalDateTime(date), seconds));
    }

    public static Date addMinutes(Date date, int minutes) {
        return toDate(addMinutes(toLocalDateTime(date), minutes));
    }

    public static Date addHours(Date date, int hours) {
        return toDate(addHours(toLocalDateTime(date), hours));
    }

    public static Date addDays(Date date, int days) {
        return toDate(addDays(toLocalDateTime(date), days));
    }

    public static Date addWeeks(Date date, int weeks) {
        return toDate(addWeeks(toLocalDateTime(date), weeks));
    }

    public static Date addMonths(Date date, int months) {
        return toDate(addMonths(toLocalDateTime(date), months));
    }

    public static Date addYears(Date date, int years) {
        return toDate(addYears(toLocalDateTime(date), years));
    }
}
