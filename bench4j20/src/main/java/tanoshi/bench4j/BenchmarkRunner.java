package tanoshi.bench4j;

import tanoshi.bench4j.annotations.Benchmark;
import tanoshi.bench4j.annotations.BenchmarkClass;
import tanoshi.bench4j.data.*;
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

public class BenchmarkRunner<T> {

    // TODO: 01.05.2023 change response to BenchmarkingResult
    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass) throws IllegalArgumentException {
        return run(benchmarkingClass, new BenchmarkConfig());
    }

    public static <T> BenchmarkingResult run(Class<T> benchmarkingClass, BenchmarkConfig config) throws IllegalArgumentException {
        if (config == null) {
            config = new BenchmarkConfig();
        }

        if (!benchmarkingClass.isAnnotationPresent(BenchmarkClass.class)) {
            return BenchmarkingResult.fromError( "Annotation " + BenchmarkClass.class.getName() + " is not present on class " + benchmarkingClass.getName(), config.getTableOptions());
        }

        Constructor<T> constructor;
        try {
            constructor = benchmarkingClass.getConstructor();
        } catch (NoSuchMethodException e) {
            return BenchmarkingResult.fromError("Benchmarking class has no empty constructor", e);
        }
        constructor.setAccessible(true);
        T instance;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return BenchmarkingResult.fromError("Benchmarking class constructor could not get instantiated", e);
        }


        List<Method> benchMethods = new ArrayList<>();

        for (Method method : benchmarkingClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Benchmark.class)) {
                // Verifying that parameters are all the same
                if (method.getParameterCount() > 0) {
                    return BenchmarkingResult.fromError("Benchmarking methods can not have parameters", config.getTableOptions());
                }

                try {
                    method.setAccessible(true);
                    method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return BenchmarkingResult.fromError("Failed to invoke benchmarking method " + method.getName(), e);
                }

                System.out.println(method.getName());
                benchMethods.add(method);
            }
        }

        if (benchMethods.isEmpty()) {
            return BenchmarkingResult.fromError("No benchmarking methods with " + Benchmark.class.getName() + " annotation found in " + benchmarkingClass.getName(), config.getTableOptions());
        }

        BenchmarkRunner<T> runner = new BenchmarkRunner<>(config, benchmarkingClass, instance, benchMethods);
        return runner.doBenchmarks();
    }

    private final BenchmarkConfig config;

    private final Class<T> benchClass;
    private final List<Method> benchMethods;
    private final T classInstance;

    private BenchmarkRunner(BenchmarkConfig config, Class<T> benchClass, T classInstance, List<Method> benchMethods) {
        this.config = config;

        this.benchClass = benchClass;
        this.benchMethods = benchMethods;
        this.classInstance = classInstance;
    }

    private BenchmarkingResult doBenchmarks() {
        long startTime = Timer.getNanoTime();
        String err = "";
        BenchmarkingRunResult runResult = new BenchmarkingRunResult();
        //for (ITestDataProvider<TIn> dataProvider : testDataProviders) {
        runResult.addProviderResult(doPerformProviderBenchmarks("default provider"));
        //}

        System.out.println(runResult.toView(config.getTableOptions()));
        return new BenchmarkingResult(true, runResult, err, TimeConverter.toMilli(TimeUnits.NANOSECONDS,
                Timer.getElapsedNanos(startTime)), config.getTableOptions());
    }

    private ProviderResult doPerformProviderBenchmarks(String providerName) {
        System.out.printf("%n%n[%s] Running benchmarks for provider%n", providerName);
        int batchSize = calculateTargetBatchSize(providerName);

        ProviderResult providerRes = new ProviderResult(providerName);
        providerRes.setTargetBatchSize(batchSize);

        for (Method method : benchMethods) {
            String name = method.getName();
            ExecutorResult execRes = dePerformExecutorBenchmarks(method, providerName, name, batchSize);
            providerRes.addExecutorRes(execRes);
        }

        System.out.println(providerRes.toView(config.getTableOptions()));
        return providerRes;
    }

    private int calculateTargetBatchSize(String providerName) {
        System.out.printf("[%s] Calculating batch sizes%n", providerName);

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
        System.out.printf("[%s] Calculated batch size: %d%n", providerName, calcBatchSize);

        return calcBatchSize;
    }


    private ExecutorResult dePerformExecutorBenchmarks(Method method,
                                                       String providerName,
                                                       String executorName,
                                                       int targetBatchSize) {
        System.out.printf("%n[%s]/[%s] Running benchmarks for executor%n%n", providerName, executorName);
        ExecutorResult execRes = new ExecutorResult(executorName);
        WarmupResult warmupRes = doWarmup(method, executorName, targetBatchSize);
        System.out.printf("[%s] Executing benchmarks%n", executorName);
        execRes.setWarmupRes(warmupRes);
        execRes.setBatchSize(targetBatchSize);

        for (int i = 0; i < config.getBatchIterations(); i++) {
            execRes.addBatch(performBatch(method, targetBatchSize));
        }

        System.out.printf("[%s] %s%n", executorName, execRes);
        return execRes;
    }

    private WarmupResult doWarmup(Method method,
                                  String executorName,
                                  int targetBatchSize) {
        System.out.printf("[%s] Benchmark warmup%n", executorName);
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
        System.out.printf("[%s] %s%n", executorName, warmupResult);
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
                System.out.println("canceling batch (max time reached)");
                break;
            }
        }
        long totalDuration = System.nanoTime() - testStart;
        double average = Arrays.stream(times).average().orElse(-1);
        double min = Arrays.stream(times).min().orElse(-1);
        double max = Arrays.stream(times).max().orElse(-1);


        BatchResult batchResult = new BatchResult(average, totalDuration, min, max, runs);
        if (!silent) {
            System.out.println(batchResult);
        }
        return batchResult;
    }
}
