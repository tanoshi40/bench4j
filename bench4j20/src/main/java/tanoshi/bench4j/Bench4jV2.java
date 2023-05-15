package tanoshi.bench4j;

import tanoshi.bench4j.annotations.*;
import tanoshi.bench4j.data.*;
import tanoshi.bench4j.logging.BenchLogger;
import tanoshi.bench4j.provider.IBenchmarkProvider;
import tanoshi.bench4j.reflection.ReflectionHelper;
import tanoshi.bench4j.settings.BenchmarkConfig;
import tanoshi.bench4j.settings.BenchmarkParameters;
import tanoshi.logging.ILogger;
import tanoshi.utils.timer.Timer;
import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.units.time.converter.TimeConverter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: v2
//  - add heap/memory stats
//  - refactor benchmarking to be more module based
//      - and depending on the module different things will be executed or measured during the tests
//      - example: [BASE, MEMORY, DETAILED]
//          - BASE: AVR
//          - MEMORY: HEAP,GARBAGE COLLECTOR STATS
//          - DETAILED: MIN, MAX, BATCH DURATION

// TODO: refactor benchmarking parameters / options objects
// TODO: more detailed benchmarking result + merge benchRes & benchRunRes
// TODO: add providers or handling for multiple test data sets

public class Bench4jV2<T> {

    private static <T> IBenchmarkProvider<T> tryGetProvider(T instance, Field testdataField) throws IllegalArgumentException {
        IntTestdata intAnnotation = testdataField.getAnnotation(IntTestdata.class);
        if (intAnnotation != null) {
            return IBenchmarkProvider.createProvider(instance, testdataField, intAnnotation);
        }

        StrTestdata strAnnotation = testdataField.getAnnotation(StrTestdata.class);
        if (strAnnotation != null) {
            return IBenchmarkProvider.createProvider(instance, testdataField, strAnnotation);
        }

        BoolTestdata boolAnnotation = testdataField.getAnnotation(BoolTestdata.class);
        if (boolAnnotation != null) {
            return IBenchmarkProvider.createProvider(instance, testdataField, boolAnnotation);
        }

        return null;
    }

    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass) {
        return run(BenchmarkParameters.from(benchmarkingClass).build());
    }

    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass, T instance, BenchmarkConfig config, BenchLogger logger) {
        return run(BenchmarkParameters.from(benchmarkingClass).withInstance(instance).withConfig(config).withLogger(logger)
                .withConfig(config)
                .withLogger(logger)
                .withInstance(instance)
                .build());
    }

    public static <T> BenchmarkingResult run(BenchmarkParameters<T> args) {
        ILogger logger = args.logger();
        Class<T> benchmarkingClass = args.benchmarkingClass();
        T classInstance = args.instance();

        List<Method> testInit = ReflectionHelper.getMethodWithAnnotation(benchmarkingClass, TestdataInitializer.class);
        Method initMethod = null;
        if (testInit.size() > 1) {
            return BenchmarkingResult.fromError(new ErrorMessage("Only zero or one method with " + TestdataInitializer.class.getName() + " annotation allowed!"));
        }

        if (testInit.size() == 1) {
            initMethod = testInit.get(0);
            initMethod.setAccessible(true);

            if (initMethod.getParameterCount() != 0) {
                return BenchmarkingResult.fromError(
                        new ErrorMessage("TestdataInitMethod [" + initMethod.getName() + "] must have no parameters: Actual parameter count" + initMethod.getParameterCount()));
            }
        }

        List<IBenchmarkProvider<T>> providers = new ArrayList<>();
        for (Field field : classInstance.getClass().getDeclaredFields()) {
            initMethod = testInit.get(0);

            if (initMethod.getParameterCount() != 0) {
                return BenchmarkingResult.fromError(
                        new ErrorMessage("TestdataInitMethod [" + initMethod.getName() + "] must have no parameters: Actual parameter count" + initMethod.getParameterCount()));
            }
            try {
                IBenchmarkProvider<T> provider = tryGetProvider(classInstance, field);
                if (provider != null) {
                    providers.add(provider);
                }
            } catch (IllegalArgumentException e) {
                return BenchmarkingResult.fromError(new ErrorMessage("Invalid init testdata method", e));
            }
        }

        if (providers.size() == 0) {
            providers.add(IBenchmarkProvider.emptyProvider());
        }

        logger.info("Finding benchmarking methods");
        List<Method> benchMethods = ReflectionHelper.getMethodWithAnnotation(benchmarkingClass, Benchmark.class);
        if (benchMethods.isEmpty()) {
            return BenchmarkingResult.fromError(
                    new ErrorMessage("No benchmarking methods with " + Benchmark.class.getName() + " annotation found in " + benchmarkingClass.getTypeName()),
                    logger);
        }

        for (Method method : benchMethods) {
            method.setAccessible(true);

            if (method.getParameterCount() != 0) {
                return BenchmarkingResult.fromError(
                        new ErrorMessage("BenchmarkingMethod [" + method.getName() + "] must have no parameters: Actual parameter count" + method.getParameterCount()));
            }
        }

        logger.info("Benchmarking methods: %s", benchMethods.stream().map(Method::getName).collect(Collectors.joining(", ")));

        Bench4jV2<T> runner = new Bench4jV2<>(args, benchMethods, initMethod, providers);
        return runner.doBenchmarks();
    }


    private final BenchmarkConfig config;

    private final List<Method> benchMethods;
    private final T classInstance;
    private final BenchLogger logger;
    private final Method initMethod;
    private final List<IBenchmarkProvider<T>> providers;

    private Bench4jV2(BenchmarkParameters<T> args, List<Method> benchMethods, Method initMethod, List<IBenchmarkProvider<T>> providers) {
        this.config = args.config();
        this.benchMethods = benchMethods;
        this.classInstance = args.instance();
        this.logger = args.logger();
        this.initMethod = initMethod;
        this.providers = providers;
    }

    private BenchLogger logTable(ILogger.Level level, String tableView) {
        logger.log(level, "%n%s", tableView);
        return logger;
    }

    private BenchmarkingResult doBenchmarks() {
        long startTime = Timer.getNanoTime();
        BenchmarkingRunResult runResult = new BenchmarkingRunResult();

        //for (ITestDataProvider<TIn> dataProvider : testDataProviders) {
        for (IBenchmarkProvider<T> provider : providers) {
            for (String providerName : provider) {
                if (initMethod != null) {
                    try {
                        initMethod.invoke(classInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return BenchmarkingResult.fromError(new ErrorMessage("Failed to invoke testInit method for provider [" + providerName + "]", e), logger);
                    }
                }
                runResult.addProviderResult(doPerformProviderBenchmarks(providerName));
            }
        }

        logTable(ILogger.Level.INFO, runResult.toView(config.getTableOptions())).info("Completed benchmark run");

        return new BenchmarkingResult(true, runResult, null, TimeConverter.toMilli(TimeUnits.NANOSECONDS,
                Timer.getElapsedNanos(startTime)), config.getTableOptions());
    }

    private ProviderResult doPerformProviderBenchmarks(String providerName) {
        logger.emptyLine();
        logger.withProvider(providerName);
        logger.info("Running benchmarks for provider [%s]", providerName);

        int batchSize = calculateTargetBatchSize();

        ProviderResult providerRes = new ProviderResult(providerName);
        providerRes.setTargetBatchSize(batchSize);

        for (Method method : benchMethods) {
            String name = method.getName();
            ExecutorResult execRes = dePerformExecutorBenchmarks(method, name, batchSize);
            providerRes.addExecutorRes(execRes);
        }

        logger.emptyLine();
        logTable(ILogger.Level.INFO, providerRes.toView(config.getTableOptions()));
        logger.removeProvider();
        logger.emptyLine();

        return providerRes;
    }

    private int calculateTargetBatchSize() {
        logger.info("Calculating batch sizes");

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
        logger.info("Calculated batch size: %d", calcBatchSize).emptyLine();

        return calcBatchSize;
    }

    private ExecutorResult dePerformExecutorBenchmarks(Method method, String executorName, int targetBatchSize) {
        logger.withExecutor(executorName);

        logger.info("Running benchmarks for executor");
        ExecutorResult execRes = new ExecutorResult(executorName);
        WarmupResult warmupRes = doWarmup(method, targetBatchSize);
        execRes.setWarmupRes(warmupRes);
        execRes.setBatchSize(targetBatchSize);

        logger.info("Executing benchmarks", executorName);
        for (int i = 0; i < config.getBatchIterations(); i++) {
            execRes.addBatch(performBatch(method, targetBatchSize));
        }

        logger.info(execRes).emptyLine();
        logger.removeExecutor();
        return execRes;
    }

    private WarmupResult doWarmup(Method method, int targetBatchSize) {
        logger.info("Benchmark warmup");
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
        logger.info(warmupResult);
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

