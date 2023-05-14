package tanoshi.bench4j.settings;

import tanoshi.bench4j.logging.BenchLogger;

public record BenchmarkParameters<T>(Class<T> benchmarkingClass, BenchmarkConfig config, BenchLogger logger,
                                     T instance) {

    public static <T> BenchmarkParametersBuilder<T> from(Class<T> benchmarkingClass) {
        return new BenchmarkParametersBuilder<>(benchmarkingClass);
    }
}