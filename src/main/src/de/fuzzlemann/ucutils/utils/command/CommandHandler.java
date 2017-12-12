package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.commands.*;
import de.fuzzlemann.ucutils.commands.faction.ChannelActivityCommand;
import de.fuzzlemann.ucutils.commands.faction.TeamSpeakAPIKeyCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.ASellDrugCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.DrugPriceCommand;
import de.fuzzlemann.ucutils.commands.faction.police.ASUCommand;
import de.fuzzlemann.ucutils.commands.faction.police.ModifyWantedsCommand;
import de.fuzzlemann.ucutils.commands.info.CInfoCommand;
import de.fuzzlemann.ucutils.commands.info.FCInfoCommand;
import de.fuzzlemann.ucutils.commands.info.FInfoCommand;
import de.fuzzlemann.ucutils.commands.info.InfoCommand;
import de.fuzzlemann.ucutils.commands.jobs.ADropDrinkCommand;
import de.fuzzlemann.ucutils.commands.jobs.ADropTransportCommand;
import de.fuzzlemann.ucutils.commands.jobs.AGetPizzaCommand;
import de.fuzzlemann.ucutils.commands.location.DistanceCommand;
import de.fuzzlemann.ucutils.commands.location.NearestATMCommand;
import de.fuzzlemann.ucutils.commands.location.NearestJobCommand;
import de.fuzzlemann.ucutils.commands.mobile.ACallCommand;
import de.fuzzlemann.ucutils.commands.mobile.ASMSCommand;
import de.fuzzlemann.ucutils.commands.mobile.MobileBlockCommand;
import de.fuzzlemann.ucutils.commands.mobile.MobileBlockListCommand;
import de.fuzzlemann.ucutils.commands.todo.AddToDoCommand;
import de.fuzzlemann.ucutils.commands.todo.DoneToDoCommand;
import de.fuzzlemann.ucutils.commands.todo.RemoveToDoCommand;
import de.fuzzlemann.ucutils.commands.todo.ToDoListCommand;
import lombok.SneakyThrows;
import lombok.val;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
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
        registerCommand(new ClearChatCommand());
        registerCommand(new InetTestCommand());
        registerCommand(new CalculateCommand());

        registerCommand(new ASMSCommand());
        registerCommand(new ACallCommand());

        registerCommand(new StopWatchCommand());
        registerCommand(new ClockCommand());
        registerCommand(new TimerCommand());

        registerCommand(new MobileBlockCommand());
        registerCommand(new MobileBlockListCommand());

        registerCommand(new ToDoListCommand());
        registerCommand(new AddToDoCommand());
        registerCommand(new DoneToDoCommand());
        registerCommand(new RemoveToDoCommand());

        registerCommand(new NearestJobCommand());
        registerCommand(new NearestATMCommand());
        registerCommand(new DistanceCommand());

        registerCommand(new CInfoCommand());
        registerCommand(new FCInfoCommand());
        registerCommand(new FInfoCommand());
        registerCommand(new InfoCommand());

        registerCommand(new ADropTransportCommand());
        registerCommand(new ADropDrinkCommand());
        registerCommand(new AGetPizzaCommand());

        registerCommand(new JShutdownCommand());

        registerCommand(new ChannelActivityCommand());
        registerCommand(new TeamSpeakAPIKeyCommand());

        ASUCommand asuCommand = new ASUCommand();
        registerCommand(asuCommand, asuCommand);

        ModifyWantedsCommand modifyWantedsCommand = new ModifyWantedsCommand();
        registerCommand(modifyWantedsCommand, modifyWantedsCommand);

        ASellDrugCommand aSellDrugCommand = new ASellDrugCommand();
        registerCommand(aSellDrugCommand, aSellDrugCommand);

        DrugPriceCommand drugPriceCommand = new DrugPriceCommand();
        registerCommand(drugPriceCommand, drugPriceCommand);
    }

    private static void registerCommand(CommandExecutor commandExecutor) {
        registerCommand(commandExecutor, null);
    }

    @SneakyThrows(NoSuchMethodException.class)
    private static void registerCommand(CommandExecutor commandExecutor, @Nullable TabCompletion tabCompletion) {
        Command commandAnnotation = commandExecutor.getClass().getMethod("onCommand", EntityPlayerSP.class, String[].class).getAnnotation(Command.class);
        String[] labels = commandAnnotation.labels();

        for (val label : labels) {
            ClientCommandHandler.instance.registerCommand(new BaseCommand(label, tabCompletion));
            COMMANDS.put(label, commandExecutor);
        }
    }

    @SneakyThrows(NoSuchMethodException.class)
    public static void issueCommand(String label, String[] args) {
        val commandExecutor = COMMANDS.get(label);
        if (commandExecutor == null) return;

        EntityPlayerSP executor = Main.MINECRAFT.player;
        Command commandAnnotation = commandExecutor.getClass().getMethod("onCommand", EntityPlayerSP.class, String[].class).getAnnotation(Command.class);

        if (commandExecutor.onCommand(executor, args)) return;

        String usage = commandAnnotation.usage();
        if (!usage.isEmpty()) {
            usage = usage.replace("%label%", label);

            val chatComponent = new TextComponentString(usage);
            chatComponent.getStyle().setColor(TextFormatting.RED);

            executor.sendMessage(chatComponent);
        }
    }
}
