package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;
import de.fuzzlemann.ucutils.utils.house.HouseParser;

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
        Class<?> objectClass = ReflectionUtil.getGenericParameter(parserClass, 0, 1);

        PARSER_REGISTRY.put(objectClass, parserClass);
    }
}
