package tanoshi.bench4j.data;

import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

public class ExecutorResult extends BatchResultSet {
    private WarmupResult warmupRes;
    private int batchSize;

    public ExecutorResult(String name) {
        super(name);
    }

    public void setWarmupRes(WarmupResult warmupRes) {
        this.warmupRes = warmupRes;
    }

    public WarmupResult getWarmupResult() {
        return warmupRes;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public String toString() {
        return String.format("Executor Result: %s (avr), %s (min), %s (max), %d (batch size)",
                TimeConverter.humanizedStr(getAvr(), TimeUnits.NANOSECONDS),
                TimeConverter.humanizedStr(getMin(), TimeUnits.NANOSECONDS),
                TimeConverter.humanizedStr(getMax(), TimeUnits.NANOSECONDS),
                batchSize
        );
    }
}
