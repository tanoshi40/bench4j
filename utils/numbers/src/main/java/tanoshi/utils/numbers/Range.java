package tanoshi.utils.numbers;

import java.util.ArrayList;
import java.util.List;

public class Range {

    private final List<Integer> values;

    private Range(List<Integer> values) {
        this.values = values;
    }

    public static Range from(int min, int max) {
        return from(min, max, 1);
    }

    public static Range from(int min, int max, int distance) {
        List<Integer> values = new ArrayList<>();
        for (int i = min; i < max; i += distance) {
            values.add(i);
        }
        return new Range(values);
    }

    public List<Integer> getValues() {
        return values;
    }
}
