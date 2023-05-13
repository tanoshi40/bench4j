package tanoshi.logging;

public class ConsoleLogger extends BaseLogger {

    public ConsoleLogger(String loggerName, String format) {
        super(loggerName, format);
    }

    public ConsoleLogger(String loggerName) {
        super(loggerName);
    }

    public ConsoleLogger(Class<?> clazz) {
        super(clazz);
    }

    public ConsoleLogger(Class<?> clazz, String format) {
        super(clazz, format);
    }

    @Override
    protected void writeMessage(String message) {
        System.out.println(message);
    }
}
