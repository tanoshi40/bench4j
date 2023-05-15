package tanoshi.utils.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class BaseLogger implements ILogger {
    protected static final String FORMAT_TIME = "<time>";
    protected static final String FORMAT_LOG_LEVEL = "<level>";
    protected static final String FORMAT_LOGGER_NAME = "<name>";
    protected static final String FORMAT_PREFIX = "<prefix>";
    protected static final String FORMAT_MESSAGE = "<msg>";

    protected static final String defaultFormat;

    static {
        defaultFormat = "%s [%s] - %s%s: %s".formatted(FORMAT_TIME, FORMAT_LOG_LEVEL, FORMAT_LOGGER_NAME, FORMAT_PREFIX, FORMAT_MESSAGE);
    }

    protected static final String defaultTimeFormat = "yyyy-MM-dd HH:mm:ss";

    protected final String loggerName;
    protected final String format;

    protected final boolean hasTimeFormat;
    protected final String timeFormat;
    protected final DateTimeFormatter dateTimeFormatter;

    protected Level loglevel = Level.INFO;
    protected String prefix = "";

    protected BaseLogger(String loggerName, String format, String timeFormat) {
        this.loggerName = loggerName;
        this.format = format;
        this.timeFormat = timeFormat;

        hasTimeFormat = format.contains(FORMAT_TIME);
        dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat);
    }

    protected BaseLogger(String loggerName, String format) {
        this(loggerName, format, defaultTimeFormat);
    }

    protected BaseLogger(String loggerName) {
        this(loggerName, defaultFormat);
    }

    protected BaseLogger(Class<?> clazz) {
        this(clazz.getName(), defaultFormat);
    }

    protected BaseLogger(Class<?> clazz, String format) {
        this(clazz.getName(), format);
    }


    @Override
    public BaseLogger debug(Object message) {
        return log(Level.DEBUG, message);
    }

    @Override
    public BaseLogger debug(String message) {
        return log(Level.DEBUG, message);
    }

    @Override
    public BaseLogger debug(String format, Object... args) {
        return log(Level.DEBUG, format, args);
    }

    @Override
    public BaseLogger info(Object message) {
        return log(Level.INFO, message);
    }

    @Override
    public BaseLogger info(String message) {
        return log(Level.INFO, message);
    }

    @Override
    public BaseLogger info(String format, Object... args) {
        return log(Level.INFO, format, args);
    }

    @Override
    public BaseLogger warn(Object message) {
        return log(Level.WARN, message);
    }

    @Override
    public BaseLogger warn(String message) {
        return log(Level.WARN, message);
    }

    @Override
    public BaseLogger warn(String format, Object... args) {
        return log(Level.WARN, format, args);
    }

    @Override
    public BaseLogger error(Object message) {
        return log(Level.ERROR, message);
    }

    @Override
    public BaseLogger error(String message) {
        return log(Level.ERROR, message);
    }

    @Override
    public BaseLogger error(String format, Object... args) {
        return log(Level.ERROR, format, args);
    }

    @Override
    public BaseLogger error(String format, Exception ex, Object... args) {
        return error(format.formatted(args), ex);
    }

    @Override
    public BaseLogger error(String message, Exception ex) {
        return log(Level.ERROR, "%s: %s%n%s".formatted(
                message,
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n")))
        );
    }


    @Override
    public BaseLogger log(Level loglevel, Object message) {
        return log(loglevel, message != null ? message.toString() : "null");
    }

    @Override
    public BaseLogger log(Level loglevel, String format, Object... args) {
        return log(loglevel, format.formatted(args));
    }

    @Override
    public BaseLogger log(Level loglevel, String message) {
        if (canLog(loglevel)) {
            writeMessage(formatMessage(loglevel, message));
        }
        return this;
    }

    private boolean canLog(Level loglevel) {
        return loglevel.biggerEquals(getLogLevel());
    }


    @Override
    public BaseLogger emptyLine(Level loglevel) {
        if (canLog(loglevel)) {
            writeMessage("");
        }
        return this;
    }

    public BaseLogger emptyLine() {
        if (canLog(Level.INFO)) {
            writeMessage("");
        }
        return this;
    }

    public BaseLogger emptyLines(int lineCount) {
        for (int i = 0; i < lineCount; i++) {
            emptyLine();
        }
        return this;
    }

    @Override
    public Level getLogLevel() {
        return loglevel;
    }

    @Override
    public BaseLogger withLogLevel(Level level) {
        loglevel = level;
        return this;
    }


    protected String formatMessage(Level loglevel, String message) {
        String formatted = format
                .replace(FORMAT_LOG_LEVEL, loglevel.toString())
                .replace(FORMAT_MESSAGE, message)
                .replace(FORMAT_LOGGER_NAME, loggerName)
                .replace(FORMAT_PREFIX, prefix);
        if (hasTimeFormat) {
            formatted = formatted.replace(FORMAT_TIME, formatTime());
        }
        return formatted;
    }

    protected String formatTime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    protected abstract BaseLogger writeMessage(String message);
}
