package tanoshi.bench4j.testclasses;

import tanoshi.bench4j.ITestDataProvider;
import tanoshi.testdata.models.Person;
import tanoshi.testdata.models.PersonList;
import tanoshi.testdata.provider.PersonProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonTestDataProvider implements ITestDataProvider<PersonList> {

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
    public PersonList getTestData() {
        return new PersonList(persons);
    }

    @Override
    public String getName() {
        return "PersonTestDataProvider-" + testDataSize;
    }

    public int getTestDataSize() {
        return testDataSize;
    }
}
