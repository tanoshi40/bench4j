package tanoshi.bench4j.data;

import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

public class WarmupResult extends BatchResultSet {

    private int reachedBatchSize;

    @Override
    public String toString() {
        return String.format("Warmup result: %s (avr), %s (min), %s (max), %d (batch size)",
                TimeConverter.humanizedStr(getAvr(), TimeUnits.NANOSECONDS),
                TimeConverter.humanizedStr(getMin(), TimeUnits.NANOSECONDS),
                TimeConverter.humanizedStr(getMax(), TimeUnits.NANOSECONDS),
                reachedBatchSize
                );
    }

    public void setReachedBatchSize(int reachedBatchSize) {
        this.reachedBatchSize = reachedBatchSize;
    }

    public int getReachedBatchSize() {
        return reachedBatchSize;
    }
}
