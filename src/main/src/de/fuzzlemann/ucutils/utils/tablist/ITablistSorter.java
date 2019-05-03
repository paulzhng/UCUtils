package de.fuzzlemann.ucutils.utils.tablist;

import com.google.common.collect.Ordering;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Fuzzlemann
 */
public interface ITablistSorter {
    void init() throws Exception;

    default void replaceOrdering(Class clazz) throws IllegalAccessException, NoSuchFieldException {
        Field field = null;

        for (Field f : clazz.getDeclaredFields()) {
            if (f.getType().equals(Ordering.class)) {
                field = f;
                break;
            }
        }

        if (field == null) return;

        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, Ordering.from(new TablistComparator()));
    }
}
