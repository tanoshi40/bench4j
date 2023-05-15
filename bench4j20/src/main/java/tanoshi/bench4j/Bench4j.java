package tanoshi.bench4j;

import org.jetbrains.annotations.NotNull;
import tanoshi.bench4j.annotations.Benchmark;
import tanoshi.bench4j.annotations.TestdataInitializer;
import tanoshi.bench4j.data.*;
import tanoshi.bench4j.exceptions.InternalBenchmarkBuildingException;
import tanoshi.bench4j.logging.BenchLogger;
import tanoshi.bench4j.provider.IBenchmarkProvider;
import tanoshi.bench4j.reflection.ReflectionHelper;
import tanoshi.bench4j.settings.BenchmarkParameters;
import tanoshi.utils.logging.ILogger;
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

public class Bench4j<T> {

    private static <T> List<IBenchmarkProvider> getProviders(T classInstance) {
        List<IBenchmarkProvider> providers = new ArrayList<>();
        for (Field field : classInstance.getClass().getDeclaredFields()) {
            IBenchmarkProvider provider = IBenchmarkProvider.tryGetProvider(classInstance, field);
            if (provider != null) {
                providers.add(provider);
            }

        }
        return providers;
    }

    private static <T> Method getTestdataInit(Class<T> benchmarkingClass) throws InternalBenchmarkBuildingException {
        List<Method> testInitializers = ReflectionHelper.getMethodWithAnnotation(benchmarkingClass, TestdataInitializer.class);
        Method initMethod = null;
        if (testInitializers.size() > 1) {
            throw new InternalBenchmarkBuildingException("Only zero or one method with %s annotation allowed!", TestdataInitializer.class.getName());
        }

        if (testInitializers.size() == 1) {
            initMethod = testInitializers.get(0);
            initMethod.setAccessible(true);

            if (initMethod.getParameterCount() != 0) {
                throw new InternalBenchmarkBuildingException("TestdataInitMethod [%s] must have no parameters: Actual parameter count %s", initMethod.getName(), initMethod.getParameterCount());
            }
        }
        return initMethod;
    }

    private static <T> List<Method> getBenchMethods(Class<T> benchmarkingClass) throws InternalBenchmarkBuildingException {
        List<Method> benchMethods = ReflectionHelper.getMethodWithAnnotation(benchmarkingClass, Benchmark.class);
        if (benchMethods.isEmpty()) {
            throw new InternalBenchmarkBuildingException("No benchmarking methods with %s annotation found in %s", Benchmark.class.getName(), benchmarkingClass.getTypeName());
        }

        for (Method method : benchMethods) {
            method.setAccessible(true);

            if (method.getParameterCount() != 0) {
                throw new InternalBenchmarkBuildingException(
                        "BenchmarkingMethod [%s] must have no parameters: Actual parameter count %s", method.getName(), method.getParameterCount());
            }
        }
        return benchMethods;
    }

    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass) {
        return run(BenchmarkParameters.from(benchmarkingClass).build());
    }

    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass, T instance, BenchLogger logger) {
        return run(BenchmarkParameters.from(benchmarkingClass).withInstance(instance)
                .withLogger(logger)
                .withLogger(logger)
                .withInstance(instance)
                .build());
    }

    public static <T> BenchmarkingResult run(@NotNull BenchmarkParameters<T> args) {
        ILogger logger = args.logger();
        Class<T> benchmarkingClass = args.benchmarkingClass();
        T classInstance = args.instance();

        Method testDataInitMethod;
        List<IBenchmarkProvider> providers;
        List<Method> benchMethods;
        try {
            logger.info("Searching for test data init method");
            testDataInitMethod = getTestdataInit(benchmarkingClass);
            if (testDataInitMethod != null) logger.info("Using test data init method %s", testDataInitMethod);
            logger.info("Searching for providers");
            providers = getProviders(classInstance);
            if (providers.size() == 0) providers.add(IBenchmarkProvider.emptyProvider());
            else logger.info("");

            logger.info("Searching for benchmarking methods");
            benchMethods = getBenchMethods(benchmarkingClass);
            logger.info("Benchmarking methods: %s", benchMethods.stream().map(Method::getName).collect(Collectors.joining(", ")));
        } catch (InternalBenchmarkBuildingException e) {
            return e.errorResult(logger);
        }

        Bench4j<T> runner = new Bench4j<>(args, benchMethods, testDataInitMethod, providers);
        return runner.doBenchmarks();
    }


    private final BenchmarkParameters<T> config;

    private final List<Method> benchMethods;
    private final T classInstance;
    private final BenchLogger logger;
    private final Method testInitMethod;
    private final List<IBenchmarkProvider> providers;

    private Bench4j(BenchmarkParameters<T> args, List<Method> benchMethods, Method testInitMethod, List<IBenchmarkProvider> providers) {
        this.config = args;
        this.benchMethods = benchMethods;
        this.classInstance = args.instance();
        this.logger = args.logger();
        this.testInitMethod = testInitMethod;
        this.providers = providers;
    }

    private BenchLogger logTable(String tableView) {
        logger.info("%n%s", tableView);
        return logger;
    }

    private BenchmarkingResult doBenchmarks() {
        long startTime = Timer.getNanoTime();
        BenchmarkingRunResult runResult = new BenchmarkingRunResult();

        //for (ITestDataProvider<TIn> dataProvider : testDataProviders) {
        for (IBenchmarkProvider provider : providers) {
            for (String providerName : provider) {
                if (testInitMethod != null) {
                    try {
                        testInitMethod.invoke(classInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return BenchmarkingResult.fromError(new ErrorMessage("Failed to invoke testInit method for provider [" + providerName + "]", e), logger);
                    }
                }
                runResult.addProviderResult(doPerformProviderBenchmarks(providerName));
            }
        }

        logTable(runResult.toView(config.tableBuilderSettings())).info("Completed benchmark run");

        return new BenchmarkingResult(true, runResult, null, TimeConverter.toMilli(TimeUnits.NANOSECONDS,
                Timer.getElapsedNanos(startTime)), config.tableBuilderSettings());
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
        logTable(providerRes.toView(config.tableBuilderSettings()));
        logger.removeProvider();
        logger.emptyLine();

        return providerRes;
    }

    private int calculateTargetBatchSize() {
        logger.info("Calculating batch sizes");

        double slowestDuration = 0;
        Method slowestExec = null;
        int j = 0;
        while (slowestDuration < TimeConverter.toNano(TimeUnits.SECONDS, config.targetBatchTimeSec()) / 2) {
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
        double targetBatchNano = TimeConverter.toNano(TimeUnits.SECONDS, config.targetBatchTimeSec());

        int calcBatchSize = Math.min((int) Math.round(targetBatchNano / calcAvrExecNanos), config.maxBatchSize());
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
        for (int i = 0; i < config.batchIterations(); i++) {
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
        for (int i = 2; batchSize <= Math.round(targetBatchSize * config.warmupFactor()); i++) {
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
        double maxDuration = TimeConverter.toNano(TimeUnits.SECONDS, config.maxBatchTimeSec());

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

