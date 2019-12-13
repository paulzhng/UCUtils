package de.fuzzlemann.ucutils.base.command.execution;

import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.base.command.exceptions.ArgumentException;
import de.fuzzlemann.ucutils.base.command.exceptions.DeclarationException;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Method;

/**
 * @author Fuzzlemann
 */
class ObjectMapper {

    static Object parseToObject(String arg, Class<?> parameterType, CommandParam commandParam) {
        Object parsedObject;
        try {
            ParameterParser<?, ?> parameterParser = getParameterParser(parameterType, commandParam);
            if (parameterParser != null) { //this check is the first when for instance a custom ParameterParser is supplied for, for example, a String
                Method parse = getParseMethod(parameterParser);

                Class<?> parserParameterType = parse.getParameterTypes()[0];
                Object input = parseToObject(arg, parserParameterType, commandParam);

                parsedObject = parse.invoke(parameterParser, input);
                if (parsedObject == null && commandParam.required()) {
                    String errorMessage = parameterParser.errorMessage();
                    if (errorMessage == null)
                        throw new ArgumentException("Required argument (" + parameterType.getSimpleName() + ") not given");

                    TextUtils.error(errorMessage);
                    throw new ArgumentException("Required argument " + parameterType.getSimpleName() + " not given", false);
                }
            } else if (ClassUtils.isAssignable(parameterType, String.class)) {
                parsedObject = arg;
            } else if (ClassUtils.isAssignable(parameterType, Integer.class)) {
                parsedObject = Integer.parseInt(arg);
            } else if (ClassUtils.isAssignable(parameterType, Boolean.class)) {
                parsedObject = commandParam.requiredValue().equalsIgnoreCase(arg);
            } else {
                throw new DeclarationException(parameterType.getName() + " is not parsable");
            }
        } catch (ArgumentException | DeclarationException e) {
            throw e;
        } catch (Exception e) {
            return null;
        }

        return parsedObject;
    }

    private static ParameterParser<?, ?> getParameterParser(Class<?> parameterType, CommandParam commandParam) throws IllegalAccessException, InstantiationException {
        Class<? extends ParameterParser> parameterParserClass = null;
        Class<? extends ParameterParser> annotatedParameterParser = commandParam.parameterParser();
        if (annotatedParameterParser != ParameterParser.class) {
            Method parse = getParseMethod(annotatedParameterParser);
            Class<?> parserParameterType = parse.getReturnType();

            if (ClassUtils.isAssignable(parameterType, parserParameterType))
                parameterParserClass = annotatedParameterParser;
        }

        if (parameterParserClass == null) {
            ParameterParser.At at = parameterType.getAnnotation(ParameterParser.At.class);
            parameterParserClass = at != null ? at.value() : ParserRegistry.PARSER_REGISTRY.get(parameterType);
        }

        if (parameterParserClass == null) return null;

        return parameterParserClass.newInstance();
    }

    private static Method getParseMethod(ParameterParser<?, ?> parameterParser) {
        return getParseMethod(parameterParser.getClass());
    }

    private static Method getParseMethod(Class<? extends ParameterParser> parameterParserClass) {
        for (Method declaredMethod : parameterParserClass.getMethods()) {
            if (declaredMethod.getName().equals("parse") && declaredMethod.getParameterTypes()[0] != Object.class) {
                return declaredMethod;
            }
        }

        throw new IllegalStateException();
    }
}
