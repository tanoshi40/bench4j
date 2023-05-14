package tanoshi.bench4j.logging;

import tanoshi.logging.ConsoleLogger;

public class BenchLogger extends ConsoleLogger {
    private boolean hasProvider = false;
    private boolean hasExecutor = false;
    private String providerName;
    private String executorName;

    public BenchLogger(String loggerName) {
        super(loggerName);
    }

    public BenchLogger withProvider(String providerName) {
        hasProvider = true;
        this.providerName = providerName;
        updatePrefix();
        return this;
    }

    public BenchLogger removeProvider(String providerName) {
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

    public BenchLogger removeExecutor(String executorName) {
        hasExecutor = false;
        updatePrefix();
        return this;
    }

    private void updatePrefix() {
        StringBuilder builder = new StringBuilder();

        if (hasProvider) {
            builder.append('[')
                    .append(providerName)
                    .append(']');
        }
        if (hasExecutor) {
            builder.append('[')
                    .append(executorName)
                    .append(']');
        }

        prefix = builder.toString();
    }
}
