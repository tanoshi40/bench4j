package tanoshi.bench4j.benchmarks;

import tanoshi.bench4j.Bench4j;
import tanoshi.bench4j.data.BenchmarkingResult;
import tanoshi.bench4j.settings.BenchmarkConfig;
import tanoshi.bench4j.testclasses.PersonTestDataProvider;
import tanoshi.bench4j.testclasses.JoinedNamesResult;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import tanoshi.bench4j.testclasses.executors.PersonTestNameListExecutor;
import tanoshi.testdata.models.PersonList;

import static org.junit.jupiter.api.Assertions.*;

public class Bench4jTest {

    private static final double testTargetBatchTime = 0.1;
    private static final BenchmarkConfig testConfig;


    static {
        testConfig = new BenchmarkConfig();
        testConfig.setTargetBatchTimeSec(testTargetBatchTime);
    }

    @Test
    public void testDoBenchmarksInvalidArgs() {
        Bench4j<PersonList, JoinedNamesResult> benchmark = new Bench4j<PersonList, JoinedNamesResult>().addTestDataProvider(new PersonTestDataProvider());
        Bench4j<PersonList, JoinedNamesResult> benchmark2 = new Bench4j<PersonList, JoinedNamesResult>().addTestExecutor(PersonTestNameListExecutor::getStringJoinerFinal, "list executor");

        BenchmarkingResult noExecsResult = benchmark.doBenchmarks();
        BenchmarkingResult noDataProvResult = benchmark2.doBenchmarks();

        assertNull(noExecsResult, "result should be null when no executors are provided");
        assertNull(noDataProvResult, "result should be null when no test data providers are added");
    }

    @Test
    public void testDoBenchmarks() {
        Bench4j<PersonList, JoinedNamesResult> benchmark = new Bench4j<PersonList, JoinedNamesResult>(testConfig)
                .addTestDataProvider(new PersonTestDataProvider())
                .addTestExecutor(PersonTestNameListExecutor::getStringJoinOptimized, "string join optimized");

        BenchmarkingResult result = benchmark.doBenchmarks();

        assertNotNull(result, "BenchmarkingResult must not be null");

        // TODO: 17/04/2023 do proper assertion
    }

    @Test
    public void testDoBenchmarksWithMultipleTestDataProvidersAndExecutors() {
        Bench4j<PersonList, JoinedNamesResult> benchmark = new Bench4j<PersonList, JoinedNamesResult>(testConfig)
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


    private static void addAllExecutors(Bench4j<PersonList, JoinedNamesResult> benchmark) {
        benchmark.addTestExecutor(PersonTestNameListExecutor::getStream, "stream");
        benchmark.addTestExecutor(PersonTestNameListExecutor::getStreamToListToStringJoin, "stream to list");
        benchmark.addTestExecutor(PersonTestNameListExecutor::getStringBuilder, "string builder");
        benchmark.addTestExecutor(PersonTestNameListExecutor::getStringJoiner, "string joiner");
        benchmark.addTestExecutor(PersonTestNameListExecutor::getStringJoin, "string join");
        benchmark.addTestExecutor(PersonTestNameListExecutor::getStringJoinOptimized, "string join optimized");
    }
}

