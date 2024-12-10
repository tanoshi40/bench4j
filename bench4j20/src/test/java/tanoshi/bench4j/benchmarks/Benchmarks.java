package tanoshi.bench4j.benchmarks;

import tanoshi.bench4j.annotations.Benchmark;
import tanoshi.bench4j.annotations.TestdataInitializer;
import tanoshi.bench4j.annotations.data.IntData;
import tanoshi.utils.testdata.executors.PersonTestNameListExecutor;
import tanoshi.utils.testdata.models.Person;
import tanoshi.utils.testdata.provider.PersonProvider;

import java.util.List;

public class Benchmarks {

    List<Person> personList;
    
    @IntData(values = {1_000, 100_000, 1_000_000})
    int size;


    @TestdataInitializer
    public void getPersons() {
        personList = PersonProvider.getPersons(size);
    }

    @Benchmark
    public String getStream() {
        return PersonTestNameListExecutor.getStream(personList);
    }

    @Benchmark
    public String getStreamToListToStringJoin() {
        return PersonTestNameListExecutor.getStreamToListToStringJoin(personList);
    }

    @Benchmark
    public String getStringBuilder() {
        return PersonTestNameListExecutor.getStringBuilder(personList);
    }

    @Benchmark
    public String getStringJoin() {
        return PersonTestNameListExecutor.getStringJoin(personList);
    }

    @Benchmark
    public String getStringJoinOptimized() {
        return PersonTestNameListExecutor.getStringJoinOptimized(personList);
    }

    @Benchmark
    public String getStringJoiner() {
        return PersonTestNameListExecutor.getStringJoiner(personList);
    }

    @Benchmark
    public String getStringJoinerFinal() {
        return PersonTestNameListExecutor.getStringJoinerFinal(personList);
    }

    @Benchmark
    public String getStringJoinOptimizedFinal() {
        return PersonTestNameListExecutor.getStringJoinOptimizedFinal(personList);
    }

    @Benchmark
    public String getStringBuilderFinal() {
        return PersonTestNameListExecutor.getStringBuilderFinal(personList);
    }
}
