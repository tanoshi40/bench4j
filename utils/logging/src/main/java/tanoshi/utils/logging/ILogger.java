package tanoshi.utils.logging;

public interface ILogger {
    ILogger info(Object message);

    ILogger info(String message);

    ILogger info(String format, Object... args);


    ILogger debug(Object message);

    ILogger debug(String message);

    ILogger debug(String format, Object... args);


    ILogger warn(Object message);

    ILogger warn(String message);

    ILogger warn(String format, Object... args);


    ILogger error(Object message);

    ILogger error(String message);

    ILogger error(String format, Object... args);

    ILogger error(String message, Exception ex);

    ILogger error(String format, Exception ex, Object... args);


    ILogger log(Level loglevel, Object message);

    ILogger log(Level loglevel, String message);

    ILogger log(Level loglevel, String format, Object... args);


    ILogger emptyLine(Level loglevel);


    Level getLogLevel();

    ILogger withLogLevel(Level level);


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

