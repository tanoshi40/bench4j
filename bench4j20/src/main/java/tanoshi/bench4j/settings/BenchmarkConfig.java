package tanoshi.bench4j.settings;

import tanoshi.bench4j.data.BenchmarkTableBuilderSettings;

public class BenchmarkConfig {

    private double targetBatchTimeSec = 2;
    private double maxBatchTimeSec = 10;
    private int maxBatchSize = (int) Math.pow(2, 25);
    private int batchIterations = 25;
    private float warmupFactor = 0.5f;
    private BenchmarkTableBuilderSettings tableOptions = new BenchmarkTableBuilderSettings();


    public double getTargetBatchTimeSec() {
        return targetBatchTimeSec;
    }

    public void setTargetBatchTimeSec(double targetBatchTimeSec) {
        this.targetBatchTimeSec = targetBatchTimeSec;
    }

    public double getMaxBatchTimeSec() {
        return maxBatchTimeSec;
    }

    public void setMaxBatchTimeSec(double maxBatchTimeSec) {
        this.maxBatchTimeSec = maxBatchTimeSec;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    public int getBatchIterations() {
        return batchIterations;
    }

    public void setBatchIterations(int batchIterations) {
        this.batchIterations = batchIterations;
    }

    public float getWarmupFactor() {
        return warmupFactor;
    }

    public void setWarmupFactor(float warmupFactor) {
        this.warmupFactor = warmupFactor;
    }

    public BenchmarkTableBuilderSettings getTableOptions() {
        return tableOptions;
    }

    public void setTableOptions(BenchmarkTableBuilderSettings tableOptions) {
        this.tableOptions = tableOptions;
    }
}
