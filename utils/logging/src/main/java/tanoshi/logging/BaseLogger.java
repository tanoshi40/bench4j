package tanoshi.logging;

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
    public void debug(Object message) {
        log(Level.DEBUG, message);
    }

    @Override
    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    @Override
    public void debug(String format, Object... args) {
        log(Level.DEBUG, format, args);
    }

    @Override
    public void info(Object message) {
        log(Level.INFO, message);
    }

    @Override
    public void info(String message) {
        log(Level.INFO, message);
    }

    @Override
    public void info(String format, Object... args) {
        log(Level.INFO, format, args);
    }

    @Override
    public void warn(Object message) {
        log(Level.WARN, message);
    }

    @Override
    public void warn(String message) {
        log(Level.WARN, message);
    }

    @Override
    public void warn(String format, Object... args) {
        log(Level.WARN, format, args);
    }

    @Override
    public void error(Object message) {
        log(Level.ERROR, message);
    }

    @Override
    public void error(String message) {
        log(Level.ERROR, message);
    }

    @Override
    public void error(String format, Object... args) {
        log(Level.ERROR, format, args);
    }

    @Override
    public void error(String format, Exception ex, Object... args) {
        error(format.formatted(args), ex);
    }

    @Override
    public void error(String message, Exception ex) {
        log(Level.ERROR, "%s: %s%n%s".formatted(
                message,
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n")))
        );
    }


    @Override
    public void log(Level loglevel, Object message) {
        log(loglevel, message != null ? message.toString() : "null");
    }

    @Override
    public void log(Level loglevel, String format, Object... args) {
        log(loglevel, format.formatted(args));
    }

    @Override
    public void log(Level loglevel, String message) {
        if (canLog(loglevel)) {
            writeMessage(formatMessage(loglevel, message));
        }
    }

    private boolean canLog(Level loglevel) {
        return loglevel.biggerEquals(getLogLevel());
    }


    @Override
    public void emptyLine(Level loglevel) {
        if (canLog(loglevel)) {
            writeMessage("");
        }
    }

    public void emptyLine() {
        if (canLog(Level.INFO)) {
            writeMessage("");
        }
    }

    @Override
    public Level getLogLevel() {
        return loglevel;
    }

    @Override
    public void setLogLevel(Level level) {
        loglevel = level;
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

    protected abstract void writeMessage(String message);
}
