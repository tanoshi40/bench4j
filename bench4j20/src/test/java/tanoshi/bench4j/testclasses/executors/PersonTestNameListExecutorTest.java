package tanoshi.bench4j.testclasses.executors;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tanoshi.testdata.models.PersonList;
import tanoshi.bench4j.testclasses.PersonTestDataProvider;

import static org.junit.jupiter.api.Assertions.*;
import static tanoshi.bench4j.testclasses.executors.PersonTestNameListExecutor.*;

@ExtendWith({SnapshotExtension.class})
class PersonTestNameListExecutorTest {
    private Expect expect;

    private static final PersonList persons = new PersonTestDataProvider(100).getTestData();

    @Test
    void getJoinedNamesStream() {
        String names = getStream(persons).namesString();
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStreamToListToStringJoin() {
        String names = getStreamToListToStringJoin(persons).namesString();
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringBuilder() {
        String names = getStringBuilder(persons).namesString();
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringJoin() {
        String names = getStringJoin(persons).namesString();
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringJoinOptimized() {
        String names = getStringJoinOptimized(persons).namesString();
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNamesStringJoiner() {
        String names = getStringJoiner(persons).namesString();
        expect.toMatchSnapshot(names);
    }

    @Test
    void getJoinedNameExecutorsAreEqual() {
        String stream = getStream(persons).namesString();
        String streamToList = getStreamToListToStringJoin(persons).namesString();
        String stringJoin = getStringJoin(persons).namesString();
        String stringBuilder = getStringBuilder(persons).namesString();
        String stringJoiner = getStringJoiner(persons).namesString();
        String stringJoinOptimized = getStringJoinOptimized(persons).namesString();

        assertEquals(stream, streamToList);
        assertEquals(stream, stringJoin);
        assertEquals(stream, stringBuilder);
        assertEquals(stream, stringJoiner);
        assertEquals(stream, stringJoinOptimized);
    }
}