package tanoshi.utils.testdata.provider;

import tanoshi.utils.testdata.ITestDataProvider;
import tanoshi.utils.testdata.models.Person;

import java.util.List;

public class PersonTestDataProvider implements ITestDataProvider<List<Person>> {

    private final int testDataSize;
    private final List<Person> persons;

    public PersonTestDataProvider() { this(10); }
    public PersonTestDataProvider(int testDataSize) {
        System.out.println("init person data provider");
        this.testDataSize = testDataSize;
        this.persons = PersonProvider.getPersons(testDataSize);
        System.out.println("init person data provider done");
    }

    @Override
    public List<Person> getTestData() {
        return persons;
    }

    @Override
    public String getName() {
        return "PersonTestDataProvider-" + testDataSize;
    }

    public int getTestDataSize() {
        return testDataSize;
    }
}
