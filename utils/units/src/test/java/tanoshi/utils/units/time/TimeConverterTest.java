package tanoshi.utils.units.time;


import org.junit.jupiter.api.Test;
import tanoshi.utils.units.time.converter.TimeConverter;

import static org.junit.jupiter.api.Assertions.*;


public class TimeConverterTest {
    static final double nano = 12087125976251D;
    static final double micro = 12087125976.251001;
    static final double milli = 12087125.976251001;
    static final double sec = 12087.125976251;
    static final double min = 201.45209960418333;
    static final double hour = 3.3575349934030556;
    static final double day = 0.13989729139179399;

    static final double allowedDelta = 0.01;

    @Test
    public void testNano() {
        NanosecondUnit unit = TimeConverter.getNanoUnit();
        double testData = nano;

        assertEquals(nano, unit.toNano(testData), allowedDelta);
        assertEquals(micro, unit.toMicro(testData), allowedDelta);
        assertEquals(milli, unit.toMilli(testData), allowedDelta);
        assertEquals(sec, unit.toSec(testData), allowedDelta);
        assertEquals(min, unit.toMin(testData), allowedDelta);
        assertEquals(hour, unit.toHour(testData), allowedDelta);
        assertEquals(day, unit.toDay(testData), allowedDelta);
    }

    @Test
    public void testMicro() {
        MicrosecondUnit unit = TimeConverter.getMicroUnit();
        double testData = micro;

        assertEquals(nano, unit.toNano(testData), allowedDelta);
        assertEquals(micro, unit.toMicro(testData), allowedDelta);
        assertEquals(milli, unit.toMilli(testData), allowedDelta);
        assertEquals(sec, unit.toSec(testData), allowedDelta);
        assertEquals(min, unit.toMin(testData), allowedDelta);
        assertEquals(hour, unit.toHour(testData), allowedDelta);
        assertEquals(day, unit.toDay(testData), allowedDelta);
    }

    @Test
    public void testMilli() {
        MillisecondUnit unit = TimeConverter.getMilliUnit();
        double testData = milli;

        assertEquals(nano, unit.toNano(testData), allowedDelta);
        assertEquals(micro, unit.toMicro(testData), allowedDelta);
        assertEquals(milli, unit.toMilli(testData), allowedDelta);
        assertEquals(sec, unit.toSec(testData), allowedDelta);
        assertEquals(min, unit.toMin(testData), allowedDelta);
        assertEquals(hour, unit.toHour(testData), allowedDelta);
        assertEquals(day, unit.toDay(testData), allowedDelta);
    }

    @Test
    public void testSec() {
        SecondUnit unit = TimeConverter.getSecUnit();
        double testData = sec;

        assertEquals(nano, unit.toNano(testData), allowedDelta);
        assertEquals(micro, unit.toMicro(testData), allowedDelta);
        assertEquals(milli, unit.toMilli(testData), allowedDelta);
        assertEquals(sec, unit.toSec(testData), allowedDelta);
        assertEquals(min, unit.toMin(testData), allowedDelta);
        assertEquals(hour, unit.toHour(testData), allowedDelta);
        assertEquals(day, unit.toDay(testData), allowedDelta);
    }

    @Test
    public void testMin() {
        MinuteUnit unit = TimeConverter.getMinUnit();
        double testData = min;

        assertEquals(nano, unit.toNano(testData), allowedDelta);
        assertEquals(micro, unit.toMicro(testData), allowedDelta);
        assertEquals(milli, unit.toMilli(testData), allowedDelta);
        assertEquals(sec, unit.toSec(testData), allowedDelta);
        assertEquals(min, unit.toMin(testData), allowedDelta);
        assertEquals(hour, unit.toHour(testData), allowedDelta);
        assertEquals(day, unit.toDay(testData), allowedDelta);
    }

    @Test
    public void testHour() {
        HourUnit unit = TimeConverter.getHourUnit();
        double testData = hour;

        assertEquals(nano, unit.toNano(testData), allowedDelta);
        assertEquals(micro, unit.toMicro(testData), allowedDelta);
        assertEquals(milli, unit.toMilli(testData), allowedDelta);
        assertEquals(sec, unit.toSec(testData), allowedDelta);
        assertEquals(min, unit.toMin(testData), allowedDelta);
        assertEquals(hour, unit.toHour(testData), allowedDelta);
        assertEquals(day, unit.toDay(testData), allowedDelta);
    }

    @Test
    public void testDay() {
        DayUnit unit = TimeConverter.getDayUnit();
        double testData = day;

        assertEquals(nano, unit.toNano(testData), allowedDelta);
        assertEquals(micro, unit.toMicro(testData), allowedDelta);
        assertEquals(milli, unit.toMilli(testData), allowedDelta);
        assertEquals(sec, unit.toSec(testData), allowedDelta);
        assertEquals(min, unit.toMin(testData), allowedDelta);
        assertEquals(hour, unit.toHour(testData), allowedDelta);
        assertEquals(day, unit.toDay(testData), allowedDelta);
    }

    @Test
    public void testHumanString() {
        double minToHour = 1000; // 16.6 h
        double secToDay = 1000000; // 11.5 day
        double msToMic = 0.1125; // 112.5 micro
        double secToSec = 3; // 3 sec
        double nanoToSec = 1572735500; // 1.5 sec

        String humanString = TimeConverter.getMinUnit().toHumanString(minToHour);
        assertTrue(humanString.startsWith("16"));
        assertTrue(humanString.endsWith(TimeConverter.getHourUnit().shortName()));

        humanString = TimeConverter.getSecUnit().toHumanString(secToDay);
        assertTrue(humanString.startsWith("11"));
        assertTrue(humanString.endsWith(TimeConverter.getDayUnit().shortName()));

        humanString = TimeConverter.getMilliUnit().toHumanString(msToMic);
        assertTrue(humanString.startsWith("112"));
        assertTrue(humanString.endsWith(TimeConverter.getMicroUnit().shortName()));

        humanString = TimeConverter.getSecUnit().toHumanString(secToSec);
        assertTrue(humanString.startsWith("3"));
        assertTrue(humanString.endsWith(TimeConverter.getSecUnit().shortName()));

        humanString = TimeConverter.getNanoUnit().toHumanString(nanoToSec);
        assertTrue(humanString.startsWith("1"));
        assertTrue(humanString.endsWith(TimeConverter.getSecUnit().shortName()));
    }
}