package tanoshi.bench4j;

import tanoshi.bench4j.data.BenchmarkTableBuilderSettings;
import tanoshi.bench4j.data.BenchmarkingRunResult;
import tanoshi.logging.ILogger;
import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

public class BenchmarkingResult {
    public static BenchmarkingResult fromError(ErrorMessage error) {
        return fromError(error, null);
    }

    public static BenchmarkingResult fromError(ErrorMessage error, ILogger logger) {
        String message = error.getMessage();
        if (logger != null) logger.error(message);
        return new BenchmarkingResult(false, null, message, -1, null);
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

    @Override
    public String toString() {
        return toString(displayOptions);
    }

    public String toString(BenchmarkTableBuilderSettings displayOptions) {
        if (!success) {
            return "BenchmarkingResult{success=%s, errorMessage='%s'}".formatted(false, errorMessage);
        }

        return "Benchmarking duration: %s%nBenchmarking result:%n%s".formatted(
                TimeConverter.humanizedStr(elapsedMillis, TimeUnits.MILLISECONDS),
                runResult.toView(displayOptions));
    }
}
