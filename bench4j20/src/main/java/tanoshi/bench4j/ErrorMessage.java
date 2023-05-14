package tanoshi.bench4j;


import java.util.Arrays;
import java.util.stream.Collectors;

public class ErrorMessage {
    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public ErrorMessage(String message, Exception e) {
        this(message + ": " + e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
    }

    public ErrorMessage(Exception e) {
        this(e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
    }

    public String getMessage() {
        return message;
    }

}
