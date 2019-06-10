package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Fuzzlemann
 */
@ExtendWith(MockitoExtension.class)
public class CommandTest {

    @BeforeAll
    static void setUp() {
        ASMDataTable asmDataTable = mock(ASMDataTable.class);

        Set<ASMDataTable.ASMData> asmDataSet = new HashSet<>();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("de.fuzzlemann.ucutils.commands"))
                .setScanners(new MethodAnnotationsScanner()));
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Command.class);
        for (Method method : methodsAnnotatedWith) {
            Class<?> commandClass = method.getDeclaringClass();

            ASMDataTable.ASMData asmData = mock(ASMDataTable.ASMData.class);
            when(asmData.getClassName()).thenReturn(commandClass.getCanonicalName());

            asmDataSet.add(asmData);
        }

        when(asmDataTable.getAll(Command.class.getCanonicalName())).thenReturn(asmDataSet);

        CommandRegistry.registerAllCommands(asmDataTable);
    }

    @Test
    void testCommandRegistry() {
        assertFalse(CommandRegistry.COMMAND_REGISTRY.isEmpty());
    }

    @Test
    void testNoDirectException() {
        for (String label : CommandRegistry.COMMAND_REGISTRY.keySet()) {
            CommandIssuer.issueCommand(label, new String[]{});
        }
    }
}
