package tanoshi.utils.testdata.provider;

import tanoshi.utils.testdata.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonProvider {
    private static final String[] names;

    static {
        names = new String[]{
                "alpha",
                "beta",
                "gamma",
                "delta",
                "epsilon",
                "zeta",
                "eta",
                "theta",
                "iota",
                "kappa",
                "lambda",
                "mü",
                "nü",
                "xi",
                "omikron",
                "pi",
                "rho",
                "sigma",
                "tau",
                "ypsilon",
                "phi",
                "chi",
                "psi",
                "omega"};
    }

    public static List<Person> getPersons(int size) {
        List<Person> persons = new ArrayList<>(size);

        Random rnd = new Random(1234);

        System.out.println("init person data provider");
        for (int i = 0; i < size; i++) {
            persons.add(
                    new Person(names[rnd.nextInt(names.length)], rnd.nextInt(100), i + 1)
            );
        }

        return persons;
    }
}
