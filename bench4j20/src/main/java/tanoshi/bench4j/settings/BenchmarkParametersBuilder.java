package tanoshi.bench4j.settings;

import tanoshi.bench4j.data.BenchmarkTableBuilderSettings;
import tanoshi.bench4j.logging.BenchLogger;
import tanoshi.utils.colors.Color;
import tanoshi.utils.colors.ConsoleTextColor;
import tanoshi.utils.logging.ILogger;
import tanoshi.utils.tables.settings.TableSettings;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BenchmarkParametersBuilder<T> {
    private final Class<T> benchmarkingClass;
    private BenchLogger logger = null;
    private T instance = null;

    private double targetBatchTimeSec = -1;
    private double maxBatchTimeSec = -1;
    private int maxBatchSize = -1;
    private int batchIterations = -1;
    private float warmupFactor = -1;
    private ConsoleTextColor highlightColor = null;
    private ConsoleTextColor secondaryColor = null;
    private BenchmarkTableBuilderSettings tableOptions = null;
    private ILogger.Level logLevel = null;


    public BenchmarkParametersBuilder(Class<T> benchmarkingClass) {
        this.benchmarkingClass = benchmarkingClass;
    }

    public BenchmarkParametersBuilder<T> withLogger(BenchLogger logger) {
        this.logger = logger;
        return this;
    }

    public BenchmarkParametersBuilder<T> withInstance(T instance) {
        this.instance = instance;
        return this;
    }

    public BenchmarkParametersBuilder<T> withTargetBatchTimeSec(double targetBatchTimeSec) {
        this.targetBatchTimeSec = targetBatchTimeSec;
        return this;
    }

    public BenchmarkParametersBuilder<T> withMaxBatchTimeSec(double maxBatchTimeSec) {
        this.maxBatchTimeSec = maxBatchTimeSec;
        return this;
    }

    public BenchmarkParametersBuilder<T> withMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    public BenchmarkParametersBuilder<T> withBatchIterations(int batchIterations) {
        this.batchIterations = batchIterations;
        return this;
    }

    public BenchmarkParametersBuilder<T> withWarmupFactor(float warmupFactor) {
        this.warmupFactor = warmupFactor;
        return this;
    }

    public BenchmarkParametersBuilder<T> withHighlightColor(ConsoleTextColor highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

    public BenchmarkParametersBuilder<T> withSecondaryColor(ConsoleTextColor secondaryColor) {
        this.secondaryColor = secondaryColor;
        return this;
    }

    public BenchmarkParametersBuilder<T> withTableOptions(BenchmarkTableBuilderSettings tableOptions) {
        this.tableOptions = tableOptions;
        return this;
    }

    public BenchmarkParametersBuilder<T> withLogLevel(ILogger.Level logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public BenchmarkParameters<T> build() {
        if (targetBatchTimeSec <= 0) targetBatchTimeSec = 2;
        if (maxBatchTimeSec <= 0) maxBatchTimeSec = 10;
        if (maxBatchSize <= 0) maxBatchSize = (int) Math.pow(2, 25);
        if (batchIterations <= 0) batchIterations = 25;
        if (warmupFactor <= 0) warmupFactor = 0.5f;
        if (highlightColor == null) highlightColor = Color.MAGENTA.consoleColor();
        if (secondaryColor == null) secondaryColor = Color.WHITE.consoleColor();

        if (logger == null) logger = getLogger();
        if (instance == null) {
            try {
                instance = getInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new IllegalArgumentException("Could not build benchmarking parameters, because instance could not be instantiated", e);
            }
        }
        initTableOptions();

        if (logLevel != null) logger.withLogLevel(logLevel);

        return new BenchmarkParameters<T>(benchmarkingClass, logger, instance, targetBatchTimeSec, maxBatchTimeSec, maxBatchSize, batchIterations, warmupFactor, highlightColor, secondaryColor, tableOptions);
    }

    private void initTableOptions() {
        if (tableOptions == null) tableOptions =
                new BenchmarkTableBuilderSettings()
                        .withTableSettings(new TableSettings()
                                .withContentColor(highlightColor)
                                .withLineColor(secondaryColor));

        TableSettings tableSettings = tableOptions.getTableSettings();
        if (tableSettings.getContentColor() == null) tableSettings.withContentColor(highlightColor);
        if (tableSettings.getLineColor() == null) tableSettings.withLineColor(secondaryColor);

    }

    private T getInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Creating an instance of '%s' class", benchmarkingClass.getTypeName());

        Constructor<T> constructor = benchmarkingClass.getDeclaredConstructor();
        constructor.setAccessible(true);

        return constructor.newInstance();
    }

    private BenchLogger getLogger() {
        String benchName = benchmarkingClass.getName();
        return new BenchLogger(benchName, highlightColor, secondaryColor);
    }
}