package tanoshi.logging;

public interface ILogger {
    void info(Object message);

    void info(String message);

    void info(String format, Object... args);


    void debug(Object message);

    void debug(String message);

    void debug(String format, Object... args);


    void warn(Object message);

    void warn(String message);

    void warn(String format, Object... args);


    void error(Object message);

    void error(String message);

    void error(String format, Object... args);

    void error(String message, Exception ex);

    void error(String format, Exception ex, Object... args);


    void log(Level loglevel, Object message);

    void log(Level loglevel, String message);

    void log(Level loglevel, String format, Object... args);


    void emptyLine(Level loglevel);


    Level getLogLevel();

    void setLogLevel(Level level);


    enum Level {
        DEBUG(0),
        INFO(1),
        WARN(2),
        ERROR(3);

        final int val;

        Level(int val) {
            this.val = val;
        }

        boolean biggerEquals(Level level) {
            return val >= level.val;
        }
    }
}

