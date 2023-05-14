package tanoshi.bench4j;

import tanoshi.bench4j.annotations.Benchmark;
import tanoshi.bench4j.data.*;
import tanoshi.bench4j.logging.BenchLogger;
import tanoshi.bench4j.settings.BenchmarkConfig;
import tanoshi.utils.timer.Timer;
import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bench4jV2<T> {
    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass) throws IllegalArgumentException {
        return run(benchmarkingClass, new BenchmarkConfig());
    }

    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass, BenchmarkConfig config) throws IllegalArgumentException {
        if (config == null) {
            config = new BenchmarkConfig();
        }

        String benchName = benchmarkingClass.getName();
        BenchLogger logger = new BenchLogger(benchName, config.getHighlightColor(), config.getSecondaryColor());

        logger.info("Creating an instance of '%s' class", benchmarkingClass.getTypeName());
        Constructor<T> constructor;
        try {
            constructor = benchmarkingClass.getConstructor();
        } catch (NoSuchMethodException e) {
            return BenchmarkingResult.fromError(new ErrorMessage("Benchmarking class has no empty constructor", e), logger);
        }
        constructor.setAccessible(true);
        T instance;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return BenchmarkingResult.fromError(new ErrorMessage("Benchmarking class constructor could not get instantiated", e), logger);
        }

        List<Method> benchMethods = new ArrayList<>();

        for (Method method : benchmarkingClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Benchmark.class)) {
                // Verifying that parameters are all the same
                if (method.getParameterCount() > 0) {
                    return BenchmarkingResult.fromError(new ErrorMessage("Benchmarking methods can not have parameters"), logger);
                }

                try {
                    method.setAccessible(true);
                    method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return BenchmarkingResult.fromError(new ErrorMessage("Failed to invoke benchmarking method " + method.getName(), e), logger);
                }

                benchMethods.add(method);
            }
        }

        logger.info("Benchmarking methods: %s", benchMethods.stream().map(Method::getName).collect(Collectors.joining(", ")));

        if (benchMethods.isEmpty()) {
            return BenchmarkingResult.fromError(new ErrorMessage("No benchmarking methods with " + Benchmark.class.getName() + " annotation found in " + benchName), logger);
        }

        Bench4jV2<T> runner = new Bench4jV2<>(config, benchmarkingClass, instance, benchMethods, logger);
        return runner.doBenchmarks();
    }


    private final BenchmarkConfig config;

    private final Class<T> benchClass;
    private final List<Method> benchMethods;
    private final T classInstance;
    private final BenchLogger logger;

    private Bench4jV2(BenchmarkConfig config, Class<T> benchClass, T classInstance, List<Method> benchMethods, BenchLogger logger) {
        this.config = config;
        this.benchClass = benchClass;
        this.benchMethods = benchMethods;
        this.classInstance = classInstance;
        this.logger = logger;
    }

    private BenchmarkingResult doBenchmarks() {
        long startTime = Timer.getNanoTime();
        BenchmarkingRunResult runResult = new BenchmarkingRunResult();
        //for (ITestDataProvider<TIn> dataProvider : testDataProviders) {
        runResult.addProviderResult(doPerformProviderBenchmarks("default provider"));
        //}

        logger.info(runResult.toView(config.getTableOptions()));
        return new BenchmarkingResult(true, runResult, null, TimeConverter.toMilli(TimeUnits.NANOSECONDS,
                Timer.getElapsedNanos(startTime)), config.getTableOptions());
    }

    private ProviderResult doPerformProviderBenchmarks(String providerName) {
        logger.withProvider(providerName);
        logger.emptyLine();
        logger.info("Running benchmarks for provider");
        int batchSize = calculateTargetBatchSize(providerName);

        ProviderResult providerRes = new ProviderResult(providerName);
        providerRes.setTargetBatchSize(batchSize);

        for (Method method : benchMethods) {
            String name = method.getName();
            ExecutorResult execRes = dePerformExecutorBenchmarks(method, providerName, name, batchSize);
            providerRes.addExecutorRes(execRes);
        }

        logger.info(providerRes.toView(config.getTableOptions()));
        return providerRes;
    }

    private int calculateTargetBatchSize(String providerName) {
        logger.info("[%s] Calculating batch sizes%n", providerName);

        double slowestDuration = 0;
        Method slowestExec = null;
        int j = 0;
        while (slowestDuration < TimeConverter.toNano(TimeUnits.SECONDS, config.getTargetBatchTimeSec()) / 2) {
            j++;
            for (Method executor : benchMethods) {
                BatchResultSet resultSet = new BatchResultSet("calc target batch size");

                resultSet.addBatch(performBatch(executor, (int) Math.pow(2, j), true));
                if (resultSet.getAvrDuration() > slowestDuration) {
                    slowestExec = executor;
                    slowestDuration = resultSet.getAvrDuration();
                }
            }
        }

        BatchResultSet resultSet = new BatchResultSet();
        for (int i = 0; i < 5; i++) {
            resultSet.addBatch(performBatch(slowestExec, (int) Math.pow(2, j), true));
        }

        double calcAvrExecNanos = resultSet.getAvr();
        double targetBatchNano = TimeConverter.toNano(TimeUnits.SECONDS, config.getTargetBatchTimeSec());

        int calcBatchSize = Math.min((int) Math.round(targetBatchNano / calcAvrExecNanos), config.getMaxBatchSize());
        logger.info("[%s] Calculated batch size: %d%n", providerName, calcBatchSize);

        return calcBatchSize;
    }


    private ExecutorResult dePerformExecutorBenchmarks(Method method,
                                                       String providerName,
                                                       String executorName,
                                                       int targetBatchSize) {
        logger.info("%n[%s]/[%s] Running benchmarks for executor%n%n", providerName, executorName);
        ExecutorResult execRes = new ExecutorResult(executorName);
        WarmupResult warmupRes = doWarmup(method, executorName, targetBatchSize);
        logger.info("[%s] Executing benchmarks%n", executorName);
        execRes.setWarmupRes(warmupRes);
        execRes.setBatchSize(targetBatchSize);

        for (int i = 0; i < config.getBatchIterations(); i++) {
            execRes.addBatch(performBatch(method, targetBatchSize));
        }

        logger.info("[%s] %s%n", executorName, execRes);
        return execRes;
    }

    private WarmupResult doWarmup(Method method,
                                  String executorName,
                                  int targetBatchSize) {
        logger.info("[%s] Benchmark warmup%n", executorName);
        WarmupResult warmupResult = new WarmupResult();
        int batchSize = 0;
        for (int i = 2; batchSize <= Math.round(targetBatchSize * config.getWarmupFactor()); i++) {
            batchSize = (int) Math.pow(2, i);
            performBatch(method, batchSize);
        }

        for (int i = 0; i < 5; i++) {
            warmupResult.addBatch(performBatch(method, batchSize));
        }

        warmupResult.setReachedBatchSize(batchSize);
        logger.info("[%s] %s%n", executorName, warmupResult);
        return warmupResult;
    }


    private BatchResult performBatch(Method method, int runs) {
        return performBatch(method, runs, false);
    }

    private BatchResult performBatch(Method method, int runs, boolean silent) {
        long[] times = new long[runs];
        double maxDuration = TimeConverter.toNano(TimeUnits.SECONDS, config.getMaxBatchTimeSec());

        System.gc();

        long testStart = System.nanoTime();
        for (int i = 0; i < runs; i++) {
            long st = System.nanoTime();
            try {
                method.invoke(classInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            long ed = System.nanoTime();
            times[i] = ed - st;

            if (ed - testStart > maxDuration) {
                logger.info("canceling batch (max time reached)");
                break;
            }
        }
        long totalDuration = System.nanoTime() - testStart;
        double average = Arrays.stream(times).average().orElse(-1);
        double min = Arrays.stream(times).min().orElse(-1);
        double max = Arrays.stream(times).max().orElse(-1);


        BatchResult batchResult = new BatchResult(average, totalDuration, min, max, runs);
        if (!silent) {
            logger.info(batchResult);
        }
        return batchResult;
    }
}

