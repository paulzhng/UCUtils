package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;
import de.fuzzlemann.ucutils.utils.house.HouseParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
class ParserRegistry {

    static Map<Class<?>, Class<? extends ParameterParser<?, ?>>> PARSER_REGISTRY = new HashMap<>();

    static {
        registerParser(HouseParser.class);
    }

    static void registerParser(Class<? extends ParameterParser<?, ?>> parserClass) {
        ParameterizedType parameterizedType = (ParameterizedType) parserClass.getGenericInterfaces()[0];
        Type objectType = parameterizedType.getActualTypeArguments()[1];
        String className = objectType.getTypeName();

        Class<?> objectClass;
        try {
            objectClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e); //should not happen
        }

        PARSER_REGISTRY.put(objectClass, parserClass);
    }
}
