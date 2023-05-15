package tanoshi.bench4j.settings;

import tanoshi.bench4j.data.BenchmarkTableBuilderSettings;
import tanoshi.bench4j.logging.BenchLogger;
import tanoshi.utils.colors.ConsoleTextColor;

public record BenchmarkParameters<T>(
        Class<T> benchmarkingClass,
        BenchLogger logger,
        T instance,
        double targetBatchTimeSec,
        double maxBatchTimeSec,
        int maxBatchSize,
        int batchIterations,
        float warmupFactor,
        ConsoleTextColor highlightColor,
        ConsoleTextColor secondaryColor,
        BenchmarkTableBuilderSettings tableBuilderSettings) {

    public static <T> BenchmarkParametersBuilder<T> from(Class<T> benchmarkingClass) {
        return new BenchmarkParametersBuilder<>(benchmarkingClass);
    }

}