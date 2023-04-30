package tanoshi.testdata.models;

import java.util.List;

public final class PersonList {
    private final List<Person> persons;

    public PersonList(List<Person> persons) {
        this.persons = persons;
    }

    public List<Person> getPersons() {
        return persons;
    }
}
