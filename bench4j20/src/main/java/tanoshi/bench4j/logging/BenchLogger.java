package tanoshi.bench4j.logging;

import tanoshi.utils.logging.ConsoleLogger;
import tanoshi.utils.colors.AsciiColorCodes;
import tanoshi.utils.colors.ConsoleTextColor;

public class BenchLogger extends ConsoleLogger {
    private static final String benchLoggerFormat;

    private static final String FORMAT_COLOR = "<col>";
    private static final String FORMAT_COLOR_RESET = "<col_res>";

    static {
        benchLoggerFormat = "%s[%s]%s: %s%s".formatted(FORMAT_COLOR, FORMAT_LOG_LEVEL, FORMAT_PREFIX, FORMAT_MESSAGE, FORMAT_COLOR_RESET);
    }

    private boolean hasProvider = false;
    private boolean hasExecutor = false;
    private String providerName;
    private String executorName;

    private final String highlightColorCode;
    private final String secondaryColorCode;

    public BenchLogger(String loggerName, ConsoleTextColor highlightColor, ConsoleTextColor secondaryColor) {
        super(loggerName, benchLoggerFormat);
        highlightColorCode = highlightColor.getColorCode();
        secondaryColorCode = secondaryColor.getColorCode();
    }

    public BenchLogger withProvider(String providerName) {
        hasProvider = true;
        this.providerName = providerName;
        updatePrefix();
        return this;
    }

    public BenchLogger removeProvider() {
        hasProvider = false;
        updatePrefix();
        return this;
    }

    public BenchLogger withExecutor(String executorName) {
        hasExecutor = true;
        this.executorName = executorName;
        updatePrefix();
        return this;
    }

    public BenchLogger removeExecutor() {
        hasExecutor = false;
        updatePrefix();
        return this;
    }

    private void updatePrefix() {
        StringBuilder builder = new StringBuilder();

        if (hasProvider) {
            builder.append('[')
                    .append(highlightColorCode)
                    .append(providerName)
                    .append(secondaryColorCode)
                    .append(']');
        }
        if (hasExecutor) {
            builder.append('[')
                    .append(highlightColorCode)
                    .append(executorName)
                    .append(secondaryColorCode)
                    .append(']');
        }

        if (!builder.isEmpty()) {
            builder.insert(0, " ");
        }
        prefix = builder.toString();
    }

    @Override
    protected String formatMessage(Level loglevel, String message) {
        return super.formatMessage(loglevel, message)
                .replace(FORMAT_COLOR, secondaryColorCode)
                .replace(FORMAT_COLOR_RESET, AsciiColorCodes.RESET);
    }
}
