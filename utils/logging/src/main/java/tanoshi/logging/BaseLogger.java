package tanoshi.logging;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class BaseLogger implements ILogger {
    protected static final String defaultFormat = "[<lvl>] - [<name>]: <msg>";

    protected String loggerName;
    protected final String format;
    protected Level loglevel = Level.INFO;

    protected BaseLogger(String loggerName, String format) {
        this.loggerName = loggerName;
        this.format = format;
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
        if (loglevel.biggerEquals(getLogLevel())) {
            writeMessage(formatMessage(loglevel, message));
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
        return format
                .replace("<lvl>", loglevel.toString())
                .replace("<msg>", message)
                .replace("<name>", loggerName);
    }

    protected abstract void writeMessage(String message);
}
