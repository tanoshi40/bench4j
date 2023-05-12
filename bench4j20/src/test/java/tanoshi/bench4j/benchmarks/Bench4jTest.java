package tanoshi.bench4j.benchmarks;

import tanoshi.bench4j.Bench4j;
import tanoshi.bench4j.BenchmarkingResult;
import tanoshi.bench4j.settings.BenchmarkConfig;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import tanoshi.testdata.executors.PersonTestNameListExecutor;
import tanoshi.testdata.models.Person;
import tanoshi.testdata.provider.PersonTestDataProvider;

import static org.junit.jupiter.api.Assertions.*;

public class Bench4jTest {

    private static final double testTargetBatchTime = 0.1;
    private static final BenchmarkConfig testConfig;


    static {
        testConfig = new BenchmarkConfig().withTargetBatchTimeSec(testTargetBatchTime);
    }

    @Test
    public void testDoBenchmarksInvalidArgs() {
        Bench4j<List<Person>, String> benchmark = new Bench4j<List<Person>, String>()
                .addTestDataProvider(new PersonTestDataProvider());

        Bench4j<List<Person>, String> benchmark2 = new Bench4j<List<Person>, String>()
                .addTestExecutor(PersonTestNameListExecutor::getStringJoinerFinal, "list executor");

        BenchmarkingResult noExecsResult = benchmark.doBenchmarks();
        BenchmarkingResult noDataProvResult = benchmark2.doBenchmarks();

        assertFalse(noExecsResult.isSuccess(), "result should be null when no executors are provided");
        assertFalse(noDataProvResult.isSuccess(), "result should be null when no test data providers are added");
    }

    @Test
    public void testDoBenchmarks() {
        Bench4j<List<Person>, String> benchmark = new Bench4j<List<Person>, String>(testConfig)
                .addTestDataProvider(new PersonTestDataProvider())
                .addTestExecutor(PersonTestNameListExecutor::getStringJoinOptimized, "string join optimized");

        BenchmarkingResult result = benchmark.doBenchmarks();

        assertNotNull(result, "BenchmarkingResult must not be null");

        // TODO: 17/04/2023 do proper assertion
    }

    @Test
    public void testDoBenchmarksWithMultipleTestDataProvidersAndExecutors() {
        Bench4j<List<Person>, String> benchmark = new Bench4j<List<Person>, String>(testConfig)
                .addTestDataProvider(
                        Arrays.asList(
                                new PersonTestDataProvider(),
                                new PersonTestDataProvider(1_000),
                                new PersonTestDataProvider(1_000_000)));
        addAllExecutors(benchmark);

        BenchmarkingResult result = benchmark.doBenchmarks();

        assertNotNull(result, "BenchmarkingResult must not be null");

        // TODO: 17/04/2023 do proper assertion
    }


    private static void addAllExecutors(Bench4j<List<Person>, String> benchmark) {
        benchmark
                .addTestExecutor(PersonTestNameListExecutor::getStream, "stream")
                .addTestExecutor(PersonTestNameListExecutor::getStreamToListToStringJoin, "stream to list")
                .addTestExecutor(PersonTestNameListExecutor::getStringBuilder, "string builder")
                .addTestExecutor(PersonTestNameListExecutor::getStringJoiner, "string joiner")
                .addTestExecutor(PersonTestNameListExecutor::getStringJoin, "string join")
                .addTestExecutor(PersonTestNameListExecutor::getStringJoinOptimized, "string join optimized");
    }
}

