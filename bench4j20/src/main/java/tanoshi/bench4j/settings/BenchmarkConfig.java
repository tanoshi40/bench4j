package tanoshi.bench4j.settings;

import tanoshi.bench4j.data.BenchmarkTableBuilderSettings;
import tanoshi.utils.colors.Color;
import tanoshi.utils.colors.ConsoleTextColor;
import tanoshi.utils.tables.models.TableSettings;

public class BenchmarkConfig {

    private double targetBatchTimeSec = 2;
    private double maxBatchTimeSec = 10;
    private int maxBatchSize = (int) Math.pow(2, 25);
    private int batchIterations = 25;
    private float warmupFactor = 0.5f;
    private ConsoleTextColor highlightColor = Color.MAGENTA.consoleColor();
    private ConsoleTextColor secondaryColor = Color.CYAN.consoleColor();

    private BenchmarkTableBuilderSettings tableOptions = new BenchmarkTableBuilderSettings()
            .withTableSettings(new TableSettings().withContentColor(highlightColor));


    public double getTargetBatchTimeSec() {
        return targetBatchTimeSec;
    }

    public BenchmarkConfig withTargetBatchTimeSec(double targetBatchTimeSec) {
        this.targetBatchTimeSec = targetBatchTimeSec;
        return this;
    }

    public double getMaxBatchTimeSec() {
        return maxBatchTimeSec;
    }

    public BenchmarkConfig withMaxBatchTimeSec(double maxBatchTimeSec) {
        this.maxBatchTimeSec = maxBatchTimeSec;
        return this;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public BenchmarkConfig withMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    public int getBatchIterations() {
        return batchIterations;
    }

    public BenchmarkConfig withBatchIterations(int batchIterations) {
        this.batchIterations = batchIterations;
        return this;
    }

    public float getWarmupFactor() {
        return warmupFactor;
    }

    public BenchmarkConfig withWarmupFactor(float warmupFactor) {
        this.warmupFactor = warmupFactor;
        return this;
    }

    public BenchmarkTableBuilderSettings getTableOptions() {
        return tableOptions;
    }

    public BenchmarkConfig withTableOptions(BenchmarkTableBuilderSettings tableOptions) {
        this.tableOptions = tableOptions;
        return this;
    }

    public ConsoleTextColor getHighlightColor() {
        return highlightColor;
    }

    public BenchmarkConfig withHighlightColor(ConsoleTextColor highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

    public ConsoleTextColor getSecondaryColor() {
        return secondaryColor;
    }

    public BenchmarkConfig withSecondaryColor(ConsoleTextColor secondaryColor) {
        this.secondaryColor = secondaryColor;
        return this;
    }
}
