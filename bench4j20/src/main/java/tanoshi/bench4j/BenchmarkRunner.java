package tanoshi.bench4j;

import tanoshi.bench4j.annotations.Benchmark;
import tanoshi.bench4j.annotations.BenchmarkClass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BenchmarkRunner {

    // TODO: 01.05.2023 change response to BenchmarkingResult
    public static <T> String run(Class<T> benchmarkingClass) throws IllegalArgumentException {
        String response = "empty";
        if (!benchmarkingClass.isAnnotationPresent(BenchmarkClass.class)) {
            return "Annotation " + BenchmarkClass.class.getName() + " is not present on class " + benchmarkingClass.getName();
        }

        List<Method> benchMethods = new ArrayList<>();

        for (Method method : benchmarkingClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Benchmark.class)) {
                System.out.println(method.getName());
                benchMethods.add(method);
            }
        }

        if (benchMethods.isEmpty()) {
            return "No benchmarking methods with " + Benchmark.class.getName() + " annotation found in " + benchmarkingClass.getName();
        }

        Class<?>[] parameters = benchMethods.get(0).getParameterTypes();
        Class<?> returnType = benchMethods.get(0).getReturnType();

        // Verifying that parameters are all the same
        for (Method benchMethod : benchMethods) {
            if (!Arrays.equals(parameters, benchMethod.getParameterTypes())
                    || returnType != benchMethod.getReturnType()) {
                return "Benchmarking methods all have same parameters and return type";
            }
        }

        return response;
    }

}
