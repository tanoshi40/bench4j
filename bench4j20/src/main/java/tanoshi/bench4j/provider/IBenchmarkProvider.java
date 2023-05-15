package tanoshi.bench4j.provider;

import tanoshi.bench4j.annotations.BoolTestdata;
import tanoshi.bench4j.annotations.IntTestdata;
import tanoshi.bench4j.annotations.StrTestdata;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public interface IBenchmarkProvider<T> extends Iterator<String>, Iterable<String> {


    static <T> IBenchmarkProvider<T> createProvider(T classInstance, Field field, IntTestdata annotation) {
        Integer[] values = Arrays.stream(annotation.values()).boxed().toArray(Integer[]::new);

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider<T> createProvider(T classInstance, Field field, StrTestdata annotation) {
        String[] values = annotation.values();

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider<T> createProvider(T classInstance, Field field, BoolTestdata annotation) {
        Boolean[] values = {true, false};

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider<T> emptyProvider() {
        return new EmptyBenchmarkProvider<T>();
    }

}
