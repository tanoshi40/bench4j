package tanoshi.bench4j.provider;

import tanoshi.bench4j.annotations.data.*;
import tanoshi.utils.numbers.Range;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public interface IBenchmarkProvider extends Iterator<String>, Iterable<String> {

    static <T> IBenchmarkProvider tryGetProvider(T instance, Field testdataField) throws IllegalArgumentException {
        IntData intAnnotation = testdataField.getAnnotation(IntData.class);
        if (intAnnotation != null) {
            return createProvider(instance, testdataField, intAnnotation);
        }

        StrData strAnnotation = testdataField.getAnnotation(StrData.class);
        if (strAnnotation != null) {
            return createProvider(instance, testdataField, strAnnotation);
        }

        BoolData boolAnnotation = testdataField.getAnnotation(BoolData.class);
        if (boolAnnotation != null) {
            return createProvider(instance, testdataField, boolAnnotation);
        }

        LongData longAnnotation = testdataField.getAnnotation(LongData.class);
        if (longAnnotation != null) {
            return createProvider(instance, testdataField, longAnnotation);
        }

        DoubleData doubleAnnotation = testdataField.getAnnotation(DoubleData.class);
        if (doubleAnnotation != null) {
            return createProvider(instance, testdataField, doubleAnnotation);
        }

        RangeData rangeAnnotation = testdataField.getAnnotation(RangeData.class);
        if (rangeAnnotation != null) {
            return createProvider(instance, testdataField, rangeAnnotation);
        }

        EnumData enumAnnotation = testdataField.getAnnotation(EnumData.class);
        if (enumAnnotation != null) {
            return createProvider(instance, testdataField, enumAnnotation);
        }

        return null;
    }

    static <T> IBenchmarkProvider createProvider(T classInstance, Field field, StrData annotation) {
        String[] values = annotation.values();

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider createProvider(T classInstance, Field field, BoolData annotation) {
        Boolean[] values = {true, false};

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider createProvider(T classInstance, Field field, IntData annotation) {
        Integer[] values = Arrays.stream(annotation.values()).boxed().toArray(Integer[]::new);

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider createProvider(T classInstance, Field field, LongData annotation) {
        Long[] values = Arrays.stream(annotation.values()).boxed().toArray(Long[]::new);

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider createProvider(T classInstance, Field field, DoubleData annotation) {
        Double[] values = Arrays.stream(annotation.values()).boxed().toArray(Double[]::new);

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider createProvider(T classInstance, Field field, RangeData annotation) {
        Integer[] values = Range.from(annotation.min(), annotation.max(), annotation.step()).getValues().toArray(new Integer[0]);

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider createProvider(T classInstance, Field field, EnumData annotation) {
        Enum<?>[] values;
        try {
            Class<? extends Enum<?>> enumClass = annotation.value();
            Field valuesField = enumClass.getDeclaredField("$VALUES");
            valuesField.setAccessible(true);
            values = (Enum<?>[]) valuesField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return new BenchmarkProvider<>(
                annotation.title().equals("") ? field.getName() : annotation.title(),
                values, classInstance, field);
    }

    static <T> IBenchmarkProvider emptyProvider() {
        return new EmptyBenchmarkProvider<>();
    }
}
