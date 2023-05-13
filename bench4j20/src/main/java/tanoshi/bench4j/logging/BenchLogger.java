package tanoshi.bench4j.logging;

import tanoshi.logging.ConsoleLogger;

public class BenchLogger extends ConsoleLogger {
    public BenchLogger(String loggerName, String format) {
        super(loggerName, format);
    }

    public BenchLogger(String loggerName) {
        super(loggerName);
    }
}
