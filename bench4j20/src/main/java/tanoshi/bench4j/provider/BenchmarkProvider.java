package tanoshi.bench4j.provider;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Iterator;

public class BenchmarkProvider<T> implements IBenchmarkProvider<T> {
    int index = 0;
    private final String title;
    private final Object[] values;
    private final Object classInstance;
    private final Field field;

    public BenchmarkProvider(String title, Object[] values, T classInstance, Field field) {
        this.title = title;
        this.values = values;
        this.classInstance = classInstance;

        field.setAccessible(true);
        this.field = field;
    }

    @Override
    public boolean hasNext() {
        return values.length > index;
    }

    @Override
    public String next() {
        String provName = title + values[index].toString();
        setField();
        index++;
        return provName;
    }

    private void setField() {
        try {
            field.set(classInstance, values[index]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return this;
    }
}
