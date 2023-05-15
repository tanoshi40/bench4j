package tanoshi.bench4j.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionHelper {
    public static List<Method> getMethodWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList<>();

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(annotationClass)) {
                methods.add(declaredMethod);
            }
        }
        return methods;
    }

    public static List<Field> getFieldWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Field> methods = new ArrayList<>();
        for (Field declaredMethod : clazz.getDeclaredFields()) {
            if (declaredMethod.isAnnotationPresent(annotationClass)) {
                methods.add(declaredMethod);
            }
        }
        return methods;
    }
}
