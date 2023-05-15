package tanoshi.bench4j.settings;

import tanoshi.bench4j.data.BenchmarkTableBuilderSettings;
import tanoshi.utils.colors.Color;
import tanoshi.utils.colors.ConsoleTextColor;
import tanoshi.utils.tables.settings.TableSettings;

/**
 * Use {@link BenchmarkParameters} instead
 */
@Deprecated(since = "2.*")
public class BenchmarkConfig {

    private double targetBatchTimeSec;
    private double maxBatchTimeSec;
    private int maxBatchSize;
    private int batchIterations;
    private float warmupFactor;
    private ConsoleTextColor highlightColor;
    private ConsoleTextColor secondaryColor;

    private BenchmarkTableBuilderSettings tableOptions;

    public BenchmarkConfig() {
        this.targetBatchTimeSec = 2;
        this.maxBatchTimeSec = 10;
        this.maxBatchSize = (int) Math.pow(2, 25);
        this.batchIterations = 25;
        this.warmupFactor = 0.5f;
        this.highlightColor = Color.MAGENTA.consoleColor();
        this.secondaryColor = Color.WHITE.consoleColor();
        this.tableOptions = new BenchmarkTableBuilderSettings()
                .withTableSettings(new TableSettings()
                        .withContentColor(highlightColor)
                        .withLineColor(secondaryColor));
    }

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
