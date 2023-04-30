package tanoshi.utils.units.time.converter;

import tanoshi.utils.units.time.*;

public class TimeConverter {
    public static NanosecondUnit getNanoUnit() {
        return NanosecondUnit.instance;
    }

    public static MicrosecondUnit getMicroUnit() {
        return MicrosecondUnit.instance;
    }

    public static MillisecondUnit getMilliUnit() {
        return MillisecondUnit.instance;
    }

    public static SecondUnit getSecUnit() {
        return SecondUnit.instance;
    }

    public static MinuteUnit getMinUnit() {
        return MinuteUnit.instance;
    }

    public static HourUnit getHourUnit() {
        return HourUnit.instance;
    }

    public static DayUnit getDayUnit() {
        return DayUnit.instance;
    }

    public static TimeUnit getTimeUnit(TimeUnits unit) {
        return switch (unit) {
            case NANOSECONDS -> getNanoUnit();
            case MICROSECONDS -> getMicroUnit();
            case MILLISECONDS -> getMilliUnit();
            case SECONDS -> getSecUnit();
            case MINUTES -> getMinUnit();
            case HOURS -> getHourUnit();
            case DAYS -> getDayUnit();
            default -> throw new RuntimeException("Unknown timeunit");
        };
    }

    public static String humanizedStr(double value, TimeUnits unit) {
        return getTimeUnit(unit).toHumanString(value);
    }

    public static double toNano(TimeUnits timeUnit, double timeValue) {
        return getTimeUnit(timeUnit).toNano(timeValue);
    }

    public static double toMicro(TimeUnits timeUnit, double timeValue) {
        return getTimeUnit(timeUnit).toMicro(timeValue);
    }

    public static double toMilli(TimeUnits timeUnit, double timeValue) {
        return getTimeUnit(timeUnit).toMilli(timeValue);
    }

    public static double toSec(TimeUnits timeUnit, double timeValue) {
        return getTimeUnit(timeUnit).toSec(timeValue);
    }

    public static double toMin(TimeUnits timeUnit, double timeValue) {
        return getTimeUnit(timeUnit).toMin(timeValue);
    }

    public static double toHour(TimeUnits timeUnit, double timeValue) {
        return getTimeUnit(timeUnit).toHour(timeValue);
    }

    public static double toDay(TimeUnits timeUnit, double timeValue) {
        return getTimeUnit(timeUnit).toDay(timeValue);
    }

}
