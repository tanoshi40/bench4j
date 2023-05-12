package tanoshi.testdata.executors;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tanoshi.testdata.models.Person;
import tanoshi.testdata.provider.PersonTestDataProvider;

import java.util.List;

import static tanoshi.testdata.executors.PersonTestNameListExecutor.*;

@ExtendWith({SnapshotExtension.class})
class PersonTestNameListExecutorTest {
    private Expect expect;

    private static final List<Person> persons = new PersonTestDataProvider(100).getTestData();

    @Test
    void getJoinedNamesStream() {
        String names = getStream(persons);
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStreamToListToStringJoin() {
        String names = getStreamToListToStringJoin(persons);
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringBuilder() {
        String names = getStringBuilder(persons);
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringJoin() {
        String names = getStringJoin(persons);
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringJoinOptimized() {
        String names = getStringJoinOptimized(persons);
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringJoiner() {
        String names = getStringJoiner(persons);
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNameExecutorsAreEqual() {
        String stream = getStream(persons);
        String streamToList = getStreamToListToStringJoin(persons);
        String stringJoin = getStringJoin(persons);
        String stringBuilder = getStringBuilder(persons);
        String stringJoiner = getStringJoiner(persons);
        String stringJoinOptimized = getStringJoinOptimized(persons);

        Assertions.assertEquals(stream, streamToList);
        Assertions.assertEquals(stream, stringJoin);
        Assertions.assertEquals(stream, stringBuilder);
        Assertions.assertEquals(stream, stringJoiner);
        Assertions.assertEquals(stream, stringJoinOptimized);
    }
}