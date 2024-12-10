package tanoshi.bench4j.exceptions;

import tanoshi.bench4j.BenchmarkingResult;
import tanoshi.bench4j.ErrorMessage;
import tanoshi.utils.logging.ILogger;

public class InternalBenchmarkBuildingException extends Exception {

    public BenchmarkingResult errorResult(ILogger logger) {
        Throwable cause = getCause();
        String message = getMessage();

        ErrorMessage error = cause != null ? new ErrorMessage(message, cause) : new ErrorMessage(message);

        return BenchmarkingResult.fromError(error, logger);
    }

    public InternalBenchmarkBuildingException(String message) {
        super(message);
    }

    public InternalBenchmarkBuildingException(String format, Object... args) {
        super(format.formatted(args));
    }

    public InternalBenchmarkBuildingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalBenchmarkBuildingException(Throwable cause, String format, Object... args) {
        super(format.formatted(args), cause);
    }
}
