package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.commands.*;
import de.fuzzlemann.ucutils.commands.mobile.ACallCommand;
import de.fuzzlemann.ucutils.commands.mobile.ASMSCommand;
import de.fuzzlemann.ucutils.commands.mobile.MobileBlockCommand;
import de.fuzzlemann.ucutils.commands.mobile.MobileBlockListCommand;
import lombok.SneakyThrows;
import lombok.val;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CommandHandler {

    private static final Map<String, CommandExecutor> COMMANDS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @SneakyThrows
    public static void registerAllCommands() {
        registerCommand(new InetTestCommand());
        registerCommand(new CalculateCommand());
        registerCommand(new ASMSCommand());
        registerCommand(new ACallCommand());
        registerCommand(new ClearChatCommand());
        registerCommand(new StopWatchCommand());
        registerCommand(new ClockCommand());
        registerCommand(new TimerCommand());
        registerCommand(new MobileBlockCommand());
        registerCommand(new MobileBlockListCommand());
    }

    @SneakyThrows(NoSuchMethodException.class)
    private static void registerCommand(CommandExecutor commandExecutor) {
        Command commandAnnotation = commandExecutor.getClass().getMethod("onCommand", EntityPlayerSP.class, String[].class).getAnnotation(Command.class);
        String[] labels = commandAnnotation.labels();

        for (val label : labels) {
            ClientCommandHandler.instance.registerCommand(new BaseCommand(label));
            COMMANDS.put(label, commandExecutor);
        }
    }

    @SneakyThrows(NoSuchMethodException.class)
    public static void issueCommand(String label, String[] args) {
        val commandExecutor = COMMANDS.get(label);

        if (commandExecutor == null) return;

        Command commandAnnotation = commandExecutor.getClass().getMethod("onCommand", EntityPlayerSP.class, String[].class).getAnnotation(Command.class);

        EntityPlayerSP executor = Main.MINECRAFT.player;

        if (commandExecutor.onCommand(executor, args)) return;

        String usage = commandAnnotation.usage();

        if (usage.isEmpty()) return;

        usage = usage.replace("%label%", label);

        val chatComponent = new TextComponentString(usage);
        chatComponent.getStyle().setColor(TextFormatting.RED);

        executor.sendMessage(chatComponent);
    }
}
