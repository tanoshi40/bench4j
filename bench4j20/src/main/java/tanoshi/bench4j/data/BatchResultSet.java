package tanoshi.bench4j.data;

import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

import java.util.ArrayList;
import java.util.List;

public class BatchResultSet {
    // TODO: 17/04/2023 add java docs
    private final String name;

    protected final List<BatchResult> batchResults;

    public BatchResultSet(String name) {
        batchResults = new ArrayList<>();
        this.name = name;
    }
    public BatchResultSet() { this("result-set"); }

    public void addBatch(BatchResult batchResult) {
        batchResults.add(batchResult);
    }

    public double getAvr() {
        return batchResults.stream().mapToDouble(BatchResult::getAverage).average().orElse(-1);
    }

    public double getMin() {
        return batchResults.stream().mapToDouble(BatchResult::getMin).min().orElse(-1);
    }

    public double getMax() {
        return batchResults.stream().mapToDouble(BatchResult::getMax).max().orElse(-1);
    }

    public int getRuns() {
        return batchResults.size();
    }

    public double getTot() {
        return batchResults.stream().mapToDouble(BatchResult::getTestDuration).sum();
    }

    public double getAvrDuration() {
        return batchResults.stream().mapToDouble(BatchResult::getTestDuration).average().orElse(-1);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("BatchResultSet: %s (avr), %s (min), %s (max)",
                TimeConverter.humanizedStr(getAvr(), TimeUnits.NANOSECONDS),
                TimeConverter.humanizedStr(getMin(), TimeUnits.NANOSECONDS),
                TimeConverter.humanizedStr(getMax(), TimeUnits.NANOSECONDS)
        );
    }
}
