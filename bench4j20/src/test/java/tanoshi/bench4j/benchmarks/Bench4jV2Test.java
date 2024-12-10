package tanoshi.bench4j.benchmarks;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tanoshi.bench4j.Bench4j;

import static org.junit.jupiter.api.Assertions.*;

public class Bench4jV2Test {

    private static final double testTargetBatchTime = 0.05;
    private static final int allExecutorsCount = 6;
    private static final Logger log = LoggerFactory.getLogger(Bench4jV2Test.class);

    @Test
    public void testDoBenchmarks() {
        Bench4j.run(Benchmarks.class);
    }

    /*@Test
    public void testDoBenchmarksInvalidArgs() {
        Bench4j_V1<List<Person>, String> benchmark = new Bench4j_V1<List<Person>, String>()
                .addTestDataProvider(new PersonTestDataProvider());

        Bench4j_V1<List<Person>, String> benchmark2 = new Bench4j_V1<List<Person>, String>()
                .addTestExecutor(PersonTestNameListExecutor::getStringJoinerFinal, "list executor");

        BenchmarkingResult noExecsResult = benchmark.doBenchmarks();
        BenchmarkingResult noDataProvResult = benchmark2.doBenchmarks();

        assertFalse(noExecsResult.isSuccess(), "result should be null when no executors are provided");
        assertFalse(noDataProvResult.isSuccess(), "result should be null when no test data providers are added");
    }

    @Test
    public void testDoBenchmarks() {
        Bench4j_V1<List<Person>, String> benchmark = new Bench4j_V1<List<Person>, String>(testConfig)
                .addTestDataProvider(new PersonTestDataProvider())
                .addTestExecutor(PersonTestNameListExecutor::getStringJoinOptimized, "string join optimized");

        BenchmarkingResult result = benchmark.doBenchmarks();


        assertNotNull(result, "BenchmarkingResult must not be null");
        assertTrue(result.isSuccess(), "Benchmark was not successful");

        BenchmarkingRunResult runRes = result.getRunResult();
        assertNotNull(runRes, "Benchmark has no run result");

        assertEquals(1, runRes.getProviderResults().size(), "1 provider result expected");

        Optional<ProviderResult> optProvRes = runRes.getProviderResults().stream().findFirst();
        assertTrue(optProvRes.isPresent());
        assertProvider(optProvRes.get(), 1);
    }

    @Test
    public void testDoBenchmarksWithMultipleTestDataProvidersAndExecutors() {
        List<ITestDataProvider<List<Person>>> providers = Arrays.asList(
                new PersonTestDataProvider(),
                new PersonTestDataProvider(1_000),
                new PersonTestDataProvider(1_000_000));
        Bench4j_V1<List<Person>, String> benchmark = new Bench4j_V1<List<Person>, String>(testConfig)
                .addTestDataProvider(providers);
        addAllExecutors(benchmark);

        BenchmarkingResult result = benchmark.doBenchmarks();

        assertNotNull(result, "BenchmarkingResult must not be null");

        assertTrue(result.isSuccess());
        assertNotNull(result.getRunResult());
        assertEquals(providers.size(), result.getRunResult().getProviderResults().size());

        for (ProviderResult providerResult : result.getRunResult().getProviderResults()) {
            assertProvider(providerResult, allExecutorsCount);
        }
    }

    private static void assertProvider(ProviderResult providerResult, int expectedExecutors) {
        assertEquals(expectedExecutors, providerResult.getExecutorResults().size(), "1 execution result expected");

        for (BatchResultSet executorResult : providerResult.getExecutorResults()) {
            assertNotNull(executorResult);
        }

    }

    private static void addAllExecutors(Bench4j_V1<List<Person>, String> benchmark) {
        benchmark
                .addTestExecutor(PersonTestNameListExecutor::getStream, "stream")
                .addTestExecutor(PersonTestNameListExecutor::getStreamToListToStringJoin, "stream to list")
                .addTestExecutor(PersonTestNameListExecutor::getStringBuilder, "string builder")
                .addTestExecutor(PersonTestNameListExecutor::getStringJoiner, "string joiner")
                .addTestExecutor(PersonTestNameListExecutor::getStringJoin, "string join")
                .addTestExecutor(PersonTestNameListExecutor::getStringJoinOptimized, "string join optimized");
    }*/

}

