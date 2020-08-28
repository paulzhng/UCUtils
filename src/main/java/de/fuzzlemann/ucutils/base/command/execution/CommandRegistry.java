package de.fuzzlemann.ucutils.base.command.execution;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Fuzzlemann
 */
public class CommandRegistry {
    static final Map<String, Object> COMMAND_REGISTRY = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static void registerAllCommands(ASMDataTable asmDataTable) {
        Set<ASMDataTable.ASMData> asmDataSet = asmDataTable.getAll(Command.class.getCanonicalName());
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class<?> clazz = Class.forName(asmData.getClassName());
                Object command = clazz.newInstance();

                if (command instanceof TabCompletion) {
                    registerCommand(command, (TabCompletion) command);
                } else {
                    registerCommand(command);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException(e); // should not happen
            }
        }
    }

    private static void registerCommand(Object commandExecutor) {
        registerCommand(commandExecutor, null);
    }

    private static void registerCommand(Object commandExecutor, @Nullable TabCompletion tabCompletion) {
        Command commandAnnotation = CommandReflection.getCommand(commandExecutor);
        String[] labels = commandAnnotation.value();
        boolean management = commandAnnotation.management();

        for (String label : labels) {
            ClientCommandHandler.instance.registerCommand(new ForgeBaseCommand(label, tabCompletion, management));
            COMMAND_REGISTRY.put(label, commandExecutor);
        }
    }
}
