package com.hopedove.commons.utils;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * JDK8 日期工具类
 * 涵盖日常使用的日期操作
 */
public class LocalDateTimeUtil {

    //获取当前时间的LocalDateTime对象
    //LocalDateTime.now();

    //根据年月日构建LocalDateTime
    //LocalDateTime.of();

    //比较日期先后
    //LocalDateTime.now().isBefore(),
    //LocalDateTime.now().isAfter(),

    /**
     * Date转换为LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param time
     * @return
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * String转LocalDateTime
     *
     * @param dateString
     * @return
     */
    public static LocalDateTime convertStringToLDT(String dateString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return localDateTime;
    }

    /**
     * String转LocalDateTime
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static LocalDateTime convertStringToLDT(String dateString, String pattern) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(pattern));
        return localDateTime;
    }

    /**
     * 获取指定时间的默认格式
     * 默认格式 yyyy-MM-dd HH:mm:Ss
     *
     * @param time
     * @return
     */
    public static String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前日期的毫秒
     *
     * @return
     */
    public static Long getCurrentMill() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的毫秒
     *
     * @param time
     * @return
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     *
     * @param time
     * @return
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取当前时间
     * 默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatNow() {
        return formatTime(LocalDateTime.now());
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern
     * @return
     */
    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     *
     * @param time
     * @param number
     * @param field
     * @return
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     *
     * @param time
     * @param number
     * @param field
     * @return
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param startTime
     * @param endTime
     * @param field     单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) return period.getYears();
        if (field == ChronoUnit.MONTHS) return period.getYears() * 12 + period.getMonths();
        return field.between(startTime, endTime);
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     *
     * @param time
     * @return
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 把字符串转为LocalDate
     *
     * @param dateString
     * @param pattern    yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDate convertStringToYMD(String dateString, String pattern) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 把字符串转为LocalDate
     *
     * @param dateString
     * @return
     */
    public static LocalDate convertStringToYMD(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 把LocalDate转为字符串，默认格式yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String formatTime(LocalDate time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 把LocalDate转为字符串
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String formatTime(LocalDate time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当月第一天日期
     *
     * @param localDate
     * @return
     */
    public static LocalDate getFirstLocalDateOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取当月最后一天日期
     *
     * @param localDate
     * @return
     */
    public static LocalDate getLastLocalDateOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 这个月有多少天
     *
     * @param localDate
     * @return
     */
    public static int getDayNumberOfMonth(LocalDate localDate) {
        return localDate.lengthOfMonth();
    }

    public static void main(String[] args) {
        System.out.println(getCurrentMill());
        System.out.println(formatTime(LocalDateTime.now()));

        LocalDate date1 = LocalDate.of(2019, 7, 1);
        LocalDate date2 = LocalDate.of(2019, 7, 31);
//        Period period = Period.between(date1, date2);
//        System.out.println(date2.toEpochDay() - date1.toEpochDay());
//        System.out.println(date2.with(TemporalAdjusters.firstDayOfMonth()));
//        System.out.println(date2.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth());
        System.out.println(LocalDate.now().lengthOfMonth());

//        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
//        for (int i = 0; i < LocalDate.now().lengthOfMonth() -1 ; i++) {
//            localDate = localDate.plusDays(1);
//            System.out.println(localDate);
//        }
    }
}
