package tanoshi.testdata.executors;

import tanoshi.testdata.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class PersonTestNameListExecutor {
    private static final String separator = ", ";

    public static String getStream(List<Person> personList) {
        return personList.stream().map(Person::getName).collect(Collectors.joining(separator));
    }

    public static String getStreamToListToStringJoin(List<Person> personList) {
        return String.join(separator, personList.stream()
                .map(Person::getName)
                .toList());
    }

    public static String getStringBuilder(List<Person> personList) {
        StringBuilder builder = new StringBuilder();

        for (Person person : personList) {
            if (builder.isEmpty()) {
                builder.append(person.getName());
            } else {
                builder.append(separator).append(person.getName());
            }
        }

        return builder.toString();
    }

    public static String getStringJoin(List<Person> personList) {
        List<String> names = new ArrayList<>();

        for (Person person : personList) {
            names.add(person.getName());
        }

        return String.join(separator, names);
    }

    public static String getStringJoinOptimized(List<Person> personList) {
        String[] names = new String[personList.size()];

        for (int i = 0; i < personList.size(); i++) {
            names[i] = personList.get(i).getName();
        }

        return String.join(separator, names);
    }

    public static String getStringJoiner(List<Person> personList) {
        StringJoiner joiner = new StringJoiner(separator);

        for (Person person : personList) {
            joiner.add(person.getName());
        }

        return joiner.toString();
    }

    public static String getStringJoinerFinal(final List<Person> personList) {
        final StringJoiner joiner = new StringJoiner(separator);

        for (Person person : personList) {
            joiner.add(person.getName());
        }

        return joiner.toString();
    }

    public static String getStringJoinOptimizedFinal(final List<Person> personList) {
        final String[] names = new String[personList.size()];

        for (int i = 0; i < personList.size(); i++) {
            names[i] = personList.get(i).getName();
        }

        return String.join(separator, names);
    }

    public static String getStringBuilderFinal(final List<Person> personList) {
        final StringBuilder builder = new StringBuilder();

        for (Person person : personList) {
            if (!builder.isEmpty()) {
                builder.append(separator);
            }
            builder.append(person.getName());
        }

        return builder.toString();
    }
}
