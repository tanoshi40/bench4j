package tanoshi.bench4j.data;

import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

public class BatchResult {
    private final double average;
    private final double testDuration;
    private final int runs;
    private final double min;
    private final double max;

    public BatchResult(double average, double testDuration, double min, double max, int runs) {
        this.average = average;
        this.testDuration = testDuration;
        this.runs = runs;
        this.min = min;
        this.max = max;
    }

    public double getAverage() {
        return average;
    }

    public double getTestDuration() {
        return testDuration;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return String.format("%d (runs), %s (avr), %s (tot)",
                runs,
                TimeConverter.humanizedStr(average, TimeUnits.NANOSECONDS),
                TimeConverter.humanizedStr(testDuration, TimeUnits.NANOSECONDS)
        );
    }


}
