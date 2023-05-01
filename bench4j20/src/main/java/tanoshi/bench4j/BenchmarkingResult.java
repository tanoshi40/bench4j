package tanoshi.bench4j;

import tanoshi.bench4j.data.BenchmarkingRunResult;

public class BenchmarkingResult {

    private final boolean success;
    private final BenchmarkingRunResult runResult;
    private final String errorMessage;

    BenchmarkingResult(boolean success, BenchmarkingRunResult runResult, String errorMessage) {
        this.success = success;
        this.runResult = runResult;
        this.errorMessage = errorMessage;
    }
}
