package de.fuzzlemann.ucutils.base.udf;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Fuzzlemann
 */
public interface UDFLoader<T> {

    void supply(T t);

    default void supplyO(Object o, Class<?> expected, Class<?> collectionParameterClass) {
        if (collectionParameterClass != null) { //workaround for gson parse failures
            Gson gson = new Gson();
            Collection<?> collection = (Collection<?>) reParse(o, expected);

            Collection newCollection = new ArrayList<>();
            for (Object object : collection) {
                String objectJson = gson.toJson(object);
                Object parsedObject = gson.fromJson(objectJson, collectionParameterClass);

                newCollection.add(parsedObject);
            }

            supply((T) newCollection);
            return;
        }

        if (o.getClass() != expected) o = reParse(o, expected);

        try {
            supply((T) o);
        } catch (ClassCastException e) {
            throw new IllegalStateException(e);
        }
    }

    default void cleanUp() {
    }

    default Object reParse(Object object, Class<?> clazz) {
        Gson gson = new Gson();
        String json = gson.toJson(object);

        return gson.fromJson(json, clazz);
    }
}
