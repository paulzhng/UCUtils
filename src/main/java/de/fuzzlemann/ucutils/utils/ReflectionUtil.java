package de.fuzzlemann.ucutils.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Fuzzlemann
 */
public class ReflectionUtil {

    public static <T> T getAnnotation(Annotation[] annotations, Class<? extends T> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) return (T) annotation;
        }

        return null;
    }

    public static <T, V> T getValue(V object, Class<T> type) {
        try {
            Field field = getField(object.getClass(), type);
            if (field == null) return null;

            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            Logger.LOGGER.catching(e);
        }

        return null;
    }

    public static void setValue(Class<?> clazz, Class<?> type, Object value) {
        try {
            Field field = getField(clazz, type);
            if (field == null) return;

            field.set(null, value);
        } catch (IllegalAccessException e) {
            Logger.LOGGER.catching(e);
        }
    }

    public static void setValue(Object object, Class<?> type, Object value) {
        try {
            Field field = getField(object.getClass(), type);
            if (field == null) return;

            field.set(object, value);
        } catch (IllegalAccessException e) {
            Logger.LOGGER.catching(e);
        }
    }

    public static Field getField(Class<?> clazz, Class<?> type) {
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.getType().equals(type)) {
                makeAccessible(declaredField);
                return declaredField;
            }
        }

        if (clazz.getSuperclass() != null) {
            return getField(clazz.getSuperclass(), type);
        }

        return null;
    }

    public static void makeAccessible(Field field) {
        field.setAccessible(true);

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Logger.LOGGER.catching(e);
        }
    }
}
