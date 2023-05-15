package tanoshi.bench4j;

import org.jetbrains.annotations.NotNull;
import tanoshi.bench4j.data.*;
import tanoshi.bench4j.settings.BenchmarkSettingsValidationResult;
import tanoshi.bench4j.settings.BenchmarkConfig;
import tanoshi.utils.testdata.ITestDataProvider;
import tanoshi.utils.units.time.converter.TimeConverter;
import tanoshi.utils.units.time.TimeUnits;
import tanoshi.utils.timer.Timer;

import java.util.*;
import java.util.function.Function;

/**
 * Deprecated: {@link Bench4j} instead
 */
@Deprecated(since = "2.*")
public class Bench4j_V1<TIn, TOut> {
    private final Map<String, Function<TIn, TOut>> testExecutors;
    private final List<ITestDataProvider<TIn>> testDataProviders;

    private final BenchmarkConfig config;

    public Bench4j_V1(BenchmarkConfig config) {
        this.testExecutors = new HashMap<>();
        this.testDataProviders = new ArrayList<>();
        this.config = Objects.requireNonNullElseGet(config, BenchmarkConfig::new);
    }

    public Bench4j_V1() {
        this(new BenchmarkConfig());
    }

    public Bench4j_V1<TIn, TOut> addTestExecutor(@NotNull Function<TIn, TOut> executor, @NotNull String executorName) {
        Function<TIn, TOut> val = testExecutors.putIfAbsent(executorName, executor);
        if (val == null) {
            System.out.printf("Executor [%s] already exists, did not add or replace!%n", executorName);
        }
        return this;
    }

    public Bench4j_V1<TIn, TOut> addTestDataProvider(@NotNull ITestDataProvider<TIn> provider) {
        testDataProviders.add(provider);
        return this;
    }

    public Bench4j_V1<TIn, TOut> addTestDataProvider(@NotNull Collection<ITestDataProvider<TIn>> providers) {
        for (ITestDataProvider<TIn> provider : providers) {
            addTestDataProvider(provider);
        }
        return this;
    }

    public BenchmarkingResult doBenchmarks() {
        long startTime = Timer.getNanoTime();
        BenchmarkSettingsValidationResult validation = validateSetup();
        if (!validation.isValid()) {
            return BenchmarkingResult.fromError(new ErrorMessage("Invalid arguments: " + validation));
        }
        BenchmarkingRunResult runResult = doPerformanceTests();

        return new BenchmarkingResult(true, runResult, null,
                TimeConverter.toMilli(TimeUnits.NANOSECONDS, Timer.getElapsedNanos(startTime)), config.getTableOptions());
    }

    private BenchmarkSettingsValidationResult validateSetup() {
        BenchmarkSettingsValidationResult result = new BenchmarkSettingsValidationResult();

        result.setHasDataProviders(!testDataProviders.isEmpty());
        result.setHasExecutors(!testExecutors.isEmpty());

        return result;
    }

    private BenchmarkingRunResult doPerformanceTests() {
        BenchmarkingRunResult result = new BenchmarkingRunResult();
        for (ITestDataProvider<TIn> dataProvider : testDataProviders) {
            result.addProviderResult(doPerformProviderBenchmarks(dataProvider));
        }

        System.out.println(result.toView(config.getTableOptions()));
        return result;
    }

    private ProviderResult doPerformProviderBenchmarks(ITestDataProvider<TIn> dataProvider) {
        System.out.printf("%n%n[%s] Running benchmarks for provider%n", dataProvider.getName());
        int batchSize = calculateTargetBatchSize(dataProvider);

        ProviderResult providerRes = new ProviderResult(dataProvider.getName());
        providerRes.setTargetBatchSize(batchSize);

        for (Map.Entry<String, Function<TIn, TOut>> set : testExecutors.entrySet()) {
            String name = set.getKey();
            Function<TIn, TOut> exec = set.getValue();
            ExecutorResult execRes = dePerformExecutorBenchmarks(exec, dataProvider, name, batchSize);
            providerRes.addExecutorRes(execRes);
        }

        System.out.println(providerRes.toView(config.getTableOptions()));
        return providerRes;
    }

    private int calculateTargetBatchSize(ITestDataProvider<TIn> dataProvider) {
        System.out.printf("[%s] Calculating batch sizes%n", dataProvider.getName());

        double slowestDuration = 0;
        Function<TIn, TOut> slowestExec = null;
        int j = 0;
        while (slowestDuration < TimeConverter.toNano(TimeUnits.SECONDS, config.getTargetBatchTimeSec()) / 2) {
            j++;
            for (Function<TIn, TOut> executor : testExecutors.values()) {
                BatchResultSet resultSet = new BatchResultSet("calc target batch size");

                resultSet.addBatch(performBatch(executor, dataProvider, (int) Math.pow(2, j), true));
                if (resultSet.getAvrDuration() > slowestDuration) {
                    slowestExec = executor;
                    slowestDuration = resultSet.getAvrDuration();
                }
            }
        }

        BatchResultSet resultSet = new BatchResultSet();
        for (int i = 0; i < 5; i++) {
            resultSet.addBatch(performBatch(slowestExec, dataProvider, (int) Math.pow(2, j), true));
        }

        double calcAvrExecNanos = resultSet.getAvr();
        double targetBatchNano = TimeConverter.toNano(TimeUnits.SECONDS, config.getTargetBatchTimeSec());

        int calcBatchSize = Math.min((int) Math.round(targetBatchNano / calcAvrExecNanos), config.getMaxBatchSize());
        System.out.printf("[%s] Calculated batch size: %d%n", dataProvider.getName(), calcBatchSize);

        return calcBatchSize;
    }

    private ExecutorResult dePerformExecutorBenchmarks(Function<TIn, TOut> exec,
                                                       ITestDataProvider<TIn> dataProvider,
                                                       String executorName,
                                                       int targetBatchSize) {
        System.out.printf("%n[%s]/[%s] Running benchmarks for executor%n%n", dataProvider.getName(), executorName);
        ExecutorResult execRes = new ExecutorResult(executorName);
        WarmupResult warmupRes = doWarmup(exec, dataProvider, executorName, targetBatchSize);
        System.out.printf("[%s] Executing benchmarks%n", executorName);
        execRes.setWarmupRes(warmupRes);
        execRes.setBatchSize(targetBatchSize);

        for (int i = 0; i < config.getBatchIterations(); i++) {
            execRes.addBatch(performBatch(exec, dataProvider, targetBatchSize));
        }

        System.out.printf("[%s] %s%n", executorName, execRes);
        return execRes;
    }

    private WarmupResult doWarmup(Function<TIn, TOut> exec,
                                  ITestDataProvider<TIn> dataProvider,
                                  String executorName,
                                  int targetBatchSize) {
        System.out.printf("[%s] Benchmark warmup%n", executorName);
        WarmupResult warmupResult = new WarmupResult();
        int batchSize = 0;
        for (int i = 2; batchSize <= Math.round(targetBatchSize * config.getWarmupFactor()); i++) {
            batchSize = (int) Math.pow(2, i);
            performBatch(exec, dataProvider, batchSize);
        }

        for (int i = 0; i < 5; i++) {
            warmupResult.addBatch(performBatch(exec, dataProvider, batchSize));
        }

        warmupResult.setReachedBatchSize(batchSize);
        System.out.printf("[%s] %s%n", executorName, warmupResult);
        return warmupResult;
    }

    private BatchResult performBatch(Function<TIn, TOut> executor, ITestDataProvider<TIn> testDataProvider, int runs) {
        return performBatch(executor, testDataProvider, runs, false);
    }

    private BatchResult performBatch(Function<TIn, TOut> executor, ITestDataProvider<TIn> testDataProvider, int runs, boolean silent) {
        long[] times = new long[runs];
        double maxDuration = TimeConverter.toNano(TimeUnits.SECONDS, config.getMaxBatchTimeSec());

        System.gc();

        long testStart = System.nanoTime();
        for (int i = 0; i < runs; i++) {
            TIn testData = testDataProvider.getTestData();
            long st = System.nanoTime();
            executor.apply(testData);
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

    public Collection<Function<TIn, TOut>> getExecutors() {
        return testExecutors.values();
    }

    public List<ITestDataProvider<TIn>> getTestDataProviders() {
        return testDataProviders;
    }
}
