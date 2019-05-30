package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CommandHandler {

    private static final Map<String, CommandExecutor> COMMANDS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public static void registerAllCommands(ASMDataTable asmDataTable) {
        Set<ASMDataTable.ASMData> asmDataSet = asmDataTable.getAll(Command.class.getCanonicalName());
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class<?> clazz = Class.forName(asmData.getClassName());

                CommandExecutor command = (CommandExecutor) clazz.newInstance();
                if (command instanceof TabCompletion) {
                    registerCommand(command, (TabCompletion) command);
                } else {
                    registerCommand(command);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException(e); //should not happen
            }
        }
    }

    private static void registerCommand(CommandExecutor commandExecutor) {
        registerCommand(commandExecutor, null);
    }

    private static void registerCommand(CommandExecutor commandExecutor, @Nullable TabCompletion tabCompletion) {
        Command commandAnnotation = getCommand(commandExecutor);

        String[] labels = commandAnnotation.labels();

        for (String label : labels) {
            ClientCommandHandler.instance.registerCommand(new BaseCommand(label, tabCompletion, commandAnnotation.management()));
            COMMANDS.put(label, commandExecutor);
        }
    }

    static void issueCommand(String label, String[] args) {
        CommandExecutor commandExecutor = COMMANDS.get(label);
        if (commandExecutor == null) return;

        EntityPlayerSP executor = Main.MINECRAFT.player;
        Command commandAnnotation = getCommand(commandExecutor);

        if (commandExecutor.onCommand(executor, args)) return;

        String usage = commandAnnotation.usage();
        if (!usage.isEmpty()) {
            usage = usage.replace("%label%", label);

            TextUtils.error(usage);
        }
    }

    private static Command getCommand(CommandExecutor commandExecutor) {
        try {
            return commandExecutor.getClass().getMethod("onCommand", EntityPlayerSP.class, String[].class).getAnnotation(Command.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
