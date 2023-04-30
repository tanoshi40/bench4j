package tanoshi.bench4j.testclasses.executors;

import tanoshi.bench4j.testclasses.JoinedNamesResult;
import tanoshi.testdata.models.Person;
import tanoshi.testdata.models.PersonList;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class PersonTestNameListExecutor {
    private static final String separator = ", ";

    public static JoinedNamesResult getStream(PersonList personList) {
        return new JoinedNamesResult(personList.getPersons().stream().map(Person::getName).collect(Collectors.joining(separator)));
    }

    public static JoinedNamesResult getStreamToListToStringJoin(PersonList personList) {
        return new JoinedNamesResult(
                String.join(separator, personList.getPersons().stream()
                        .map(Person::getName)
                        .toList()));
    }

    public static JoinedNamesResult getStringBuilder(PersonList personList) {
        StringBuilder builder = new StringBuilder();

        for (Person person : personList.getPersons()) {
            if (builder.isEmpty()) {
                builder.append(person.getName());
            } else {
                builder.append(separator).append(person.getName());
            }
        }

        return new JoinedNamesResult(builder.toString());
    }

    public static JoinedNamesResult getStringJoin(PersonList personList) {
        List<String> names = new ArrayList<>();

        for (Person person : personList.getPersons()) {
            names.add(person.getName());
        }

        return new JoinedNamesResult(String.join(separator, names));
    }

    public static JoinedNamesResult getStringJoinOptimized(PersonList personList) {
        String[] names = new String[personList.getPersons().size()];

        List<Person> persons = personList.getPersons();
        for (int i = 0; i < persons.size(); i++) {
            names[i] = persons.get(i).getName();
        }

        return new JoinedNamesResult(String.join(separator, names));
    }

    public static JoinedNamesResult getStringJoiner(PersonList personList) {
        StringJoiner joiner = new StringJoiner(separator);

        for (Person person : personList.getPersons()) {
            joiner.add(person.getName());
        }

        return new JoinedNamesResult(joiner.toString());
    }

    public static JoinedNamesResult getStringJoinerFinal(final PersonList personList) {
        final StringJoiner joiner = new StringJoiner(separator);

        for (Person person : personList.getPersons()) {
            joiner.add(person.getName());
        }

        return new JoinedNamesResult(joiner.toString());
    }

    public static JoinedNamesResult getStringJoinOptimizedFinal(final PersonList personList) {
        final String[] names = new String[personList.getPersons().size()];
        final List<Person> persons = personList.getPersons();

        for (int i = 0; i < persons.size(); i++) {
            names[i] = persons.get(i).getName();
        }

        return new JoinedNamesResult(String.join(separator, names));
    }

    public static JoinedNamesResult getStringBuilderFinal(final PersonList personList) {
        final StringBuilder builder = new StringBuilder();

        for (Person person : personList.getPersons()) {
            if (!builder.isEmpty()) {
                builder.append(separator);
            }
            builder.append(person.getName());
        }

        return new JoinedNamesResult(builder.toString());
    }
}
