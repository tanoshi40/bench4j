package tanoshi.bench4j;

import tanoshi.bench4j.data.BenchmarkTableBuilderSettings;
import tanoshi.bench4j.data.BenchmarkingRunResult;
import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

public class BenchmarkingResult {
    public static BenchmarkingResult fromError(String message, Exception e) {
        return fromError(message + ": " + e.getMessage());
    }

    public static BenchmarkingResult fromError(Exception e) {
        return fromError(e.getMessage());
    }

    public static BenchmarkingResult fromError(String errorMessage) {
        return new BenchmarkingResult(false, null, errorMessage, -1, new BenchmarkTableBuilderSettings());
    }

    public static BenchmarkingResult fromError(String message, Exception e, BenchmarkTableBuilderSettings displayOptions) {
        return fromError(message + ": " + e.getMessage());
    }

    public static BenchmarkingResult fromError(Exception e, BenchmarkTableBuilderSettings displayOptions) {
        return fromError(e.getMessage());
    }

    public static BenchmarkingResult fromError(String errorMessage, BenchmarkTableBuilderSettings displayOptions) {
        return new BenchmarkingResult(false, null, errorMessage, -1, displayOptions);
    }

    private final boolean success;
    private final BenchmarkingRunResult runResult;
    private final String errorMessage;
    private final double elapsedMillis;
    private final BenchmarkTableBuilderSettings displayOptions;


    BenchmarkingResult(boolean success, BenchmarkingRunResult runResult, String errorMessage, double elapsedMillis, BenchmarkTableBuilderSettings displayOptions) {
        this.success = success;
        this.runResult = runResult;
        this.errorMessage = errorMessage;
        this.elapsedMillis = elapsedMillis;
        this.displayOptions = displayOptions;
    }

    public boolean isSuccess() {
        return success;
    }

    public BenchmarkingRunResult getRunResult() {
        return runResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public double getElapsedMillis() {
        return elapsedMillis;
    }

    public String toView() {
        return toView(displayOptions);
    }
    public String toView(BenchmarkTableBuilderSettings displayOptions) {
        if (!success) {
            return "BenchmarkingResult{success=%s, errorMessage='%s'}".formatted(false, errorMessage);
        }

        return "Benchmarking duration: %s%nBenchmarking result:%n%s".formatted(
                TimeConverter.humanizedStr(elapsedMillis, TimeUnits.MILLISECONDS),
                runResult.toView(displayOptions));
    }
}
