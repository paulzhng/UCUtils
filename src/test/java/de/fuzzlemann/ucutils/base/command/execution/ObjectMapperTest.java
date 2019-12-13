package de.fuzzlemann.ucutils.base.command.execution;

import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.exceptions.ArgumentException;
import de.fuzzlemann.ucutils.base.command.exceptions.DeclarationException;
import de.fuzzlemann.ucutils.base.command.execution.objectmapper.CustomObjectParser;
import de.fuzzlemann.ucutils.base.command.execution.objectmapper.DeclaredTestObject;
import de.fuzzlemann.ucutils.base.command.execution.objectmapper.GeneralParser;
import de.fuzzlemann.ucutils.base.command.execution.objectmapper.GeneralTestObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fuzzlemann
 */
class ObjectMapperTest {

    @AfterEach
    void cleanUp() {
        CommandRegistry.COMMAND_REGISTRY.clear();
    }

    @Test
    void testParseString() {
        assertEquals("test", ObjectMapper.parseToObject("test", String.class, DefaultCommandParamSupplier.COMMAND_PARAM));
        assertEquals("12421", ObjectMapper.parseToObject("12421", String.class, DefaultCommandParamSupplier.COMMAND_PARAM));
    }

    @Test
    void testParseInteger() {
        assertEquals(1421, ObjectMapper.parseToObject("1421", Integer.class, DefaultCommandParamSupplier.COMMAND_PARAM));
        assertNull(ObjectMapper.parseToObject("test", Integer.class, DefaultCommandParamSupplier.COMMAND_PARAM));
    }

    @Test
    void testParseBoolean() throws NoSuchMethodException {
        assertTrue((boolean) ObjectMapper.parseToObject("booleanTestParameterTrue", boolean.class, getBooleanCommandParam(true)));
        assertFalse((boolean) ObjectMapper.parseToObject("booleanTestParameterFalse", boolean.class, getBooleanCommandParam(true)));
    }

    @Test
    void testAnnotatedParser() throws NoSuchMethodException {
        Object parsedObject = ObjectMapper.parseToObject("annotatedParser", GeneralTestObject.class, getAnnotatedParserCommandParam(null));
        assertNotNull(parsedObject);
        assertEquals(GeneralTestObject.class, parsedObject.getClass());
        assertEquals("annotatedParser", ((GeneralTestObject) parsedObject).getString());
    }

    @Test
    void testDirectlyDeclaredParser() {
        Object parsedObject = ObjectMapper.parseToObject("directlyDeclaredParser", DeclaredTestObject.class, DefaultCommandParamSupplier.COMMAND_PARAM);
        assertNotNull(parsedObject);
        assertEquals(DeclaredTestObject.class, parsedObject.getClass());
        assertEquals("directlyDeclaredParser", ((DeclaredTestObject) parsedObject).getString());
    }

    @Test
    void testRegistryDeclaredParser() {
        ParserRegistry.registerParser(GeneralParser.class);

        Object parsedObject = ObjectMapper.parseToObject("registryDeclaredParser", GeneralTestObject.class, DefaultCommandParamSupplier.COMMAND_PARAM);
        assertNotNull(parsedObject);
        assertEquals(GeneralTestObject.class, parsedObject.getClass());
        assertEquals("registryDeclaredParser", ((GeneralTestObject) parsedObject).getString());
    }

    @Test
    void testCustomObjectAsArgument() {
        ParserRegistry.registerParser(CustomObjectParser.class);

        Object parsedObject = ObjectMapper.parseToObject("customObject", GeneralTestObject.class, DefaultCommandParamSupplier.COMMAND_PARAM);
        assertNotNull(parsedObject);
        assertEquals(GeneralTestObject.class, parsedObject.getClass());
        assertEquals("customObject", ((GeneralTestObject) parsedObject).getString());
    }

    @Test
    void testNoParseResult() {
        ParserRegistry.registerParser(CustomObjectParser.class);
        assertThrows(ArgumentException.class, () -> ObjectMapper.parseToObject(null, GeneralTestObject.class, DefaultCommandParamSupplier.COMMAND_PARAM));
    }

    @Test
    void testNoParser() {
        assertThrows(DeclarationException.class, () -> ObjectMapper.parseToObject("exceptionExpected", ObjectMapperTest.class, DefaultCommandParamSupplier.COMMAND_PARAM));
    }

    private CommandParam getBooleanCommandParam(@CommandParam(required = false, requiredValue = "booleanTestParameterTrue") boolean testBoolean) throws NoSuchMethodException {
        return (CommandParam) ObjectMapperTest.class.getDeclaredMethod("getBooleanCommandParam", boolean.class).getParameterAnnotations()[0][0];
    }

    private CommandParam getAnnotatedParserCommandParam(@CommandParam(parameterParser = GeneralParser.class) GeneralTestObject generalTestObject) throws NoSuchMethodException {
        return (CommandParam) ObjectMapperTest.class.getDeclaredMethod("getAnnotatedParserCommandParam", GeneralTestObject.class).getParameterAnnotations()[0][0];
    }
}
