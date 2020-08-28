package de.fuzzlemann.ucutils.base.command.execution;

import com.google.common.collect.ImmutableSet;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.TestPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.commands.ClearChatCommand;
import de.fuzzlemann.ucutils.commands.UpdateCommand;
import de.fuzzlemann.ucutils.commands.faction.ChannelActivityCommand;
import de.fuzzlemann.ucutils.commands.faction.CheckActiveMembersCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.drug.DrugPriceCommand;
import de.fuzzlemann.ucutils.commands.jobs.ADropDrinkCommand;
import de.fuzzlemann.ucutils.commands.jobs.ADropTransportCommand;
import de.fuzzlemann.ucutils.commands.jobs.AGetPizzaCommand;
import de.fuzzlemann.ucutils.utils.io.FileManager;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
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
class CommandTest {

    private final Set<Class<?>> excludeFromDirectExceptionTesting = ImmutableSet.of(ADropDrinkCommand.class, ADropTransportCommand.class, AGetPizzaCommand.class, ChannelActivityCommand.class, ClearChatCommand.class, DrugPriceCommand.class);

    @BeforeAll
    static void setUp() {
        UpdateCommand.modFile = new File(FileManager.MC_DIRECTORY, "ucutils.jar");

        AbstractionLayer.getInstance().setPlayerImplementation(TestPlayer.class);
        ASMDataTable asmDataTable = mock(ASMDataTable.class);

        Set<ASMDataTable.ASMData> asmDataSet = new HashSet<>();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("de.fuzzlemann.ucutils.commands"))
                .setScanners(new MethodAnnotationsScanner()));
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Command.class);
        for (Method method : methodsAnnotatedWith) {
            Class<?> commandClass = method.getDeclaringClass();

            if (commandClass.getPackage().getName().startsWith("de.fuzzlemann.ucutils.commands.teamspeak")) continue;
            if (commandClass == CheckActiveMembersCommand.class) continue;

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
}
