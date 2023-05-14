package tanoshi.bench4j.settings;

import tanoshi.bench4j.logging.BenchLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BenchmarkParametersBuilder<T> {
    private final Class<T> benchmarkingClass;
    private BenchmarkConfig config = null;
    private BenchLogger logger = null;
    private T instance = null;

    public BenchmarkParametersBuilder(Class<T> benchmarkingClass) {
        this.benchmarkingClass = benchmarkingClass;
    }

    public BenchmarkParametersBuilder<T> withConfig(BenchmarkConfig config) {
        this.config = config;
        return this;
    }

    public BenchmarkParametersBuilder<T> withLogger(BenchLogger logger) {
        this.logger = logger;
        return this;
    }

    public BenchmarkParametersBuilder<T> withInstance(T instance) {
        this.instance = instance;
        return this;
    }

    public BenchmarkParameters<T> build() {
        if (config == null) config = new BenchmarkConfig();
        if (logger == null) logger = getLogger();
        if (instance == null) {
            try {
                instance = getInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new IllegalArgumentException("Could not build benchmarking parameters, because instance could not be instantiated", e);
            }
        }

        return new BenchmarkParameters<>(benchmarkingClass, config, logger, instance);
    }


    private T getInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Creating an instance of '%s' class", benchmarkingClass.getTypeName());

        Constructor<T> constructor = benchmarkingClass.getConstructor();
        constructor.setAccessible(true);

        return constructor.newInstance();
    }

    private BenchLogger getLogger() {
        String benchName = benchmarkingClass.getName();
        return new BenchLogger(benchName, config.getHighlightColor(), config.getSecondaryColor());
    }
}