package de.fuzzlemann.ucutils.utils.command;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.commands.*;
import de.fuzzlemann.ucutils.commands.faction.CallReinforcementCommand;
import de.fuzzlemann.ucutils.commands.faction.ChannelActivityCommand;
import de.fuzzlemann.ucutils.commands.faction.CheckActiveMembersCommand;
import de.fuzzlemann.ucutils.commands.faction.SchwarzmarktLocationsCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist.ASetBlacklistCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist.BlacklistPriceCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.drug.ASellDrugCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.drug.DrugPriceCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.drug.GiveDrugCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.speech.ToggleKerzakovSpeechCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.speech.ToggleMafiaSpeechCommand;
import de.fuzzlemann.ucutils.commands.faction.badfaction.speech.ToggleTriadenSpeechCommand;
import de.fuzzlemann.ucutils.commands.faction.police.ASUCommand;
import de.fuzzlemann.ucutils.commands.faction.police.CheckMedicalLicenseCommand;
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
import de.fuzzlemann.ucutils.commands.mobile.*;
import de.fuzzlemann.ucutils.commands.supporter.PunishCommand;
import de.fuzzlemann.ucutils.commands.supporter.SendNoobChatCommand;
import de.fuzzlemann.ucutils.commands.teamspeak.MoveCommand;
import de.fuzzlemann.ucutils.commands.teamspeak.MoveHereCommand;
import de.fuzzlemann.ucutils.commands.teamspeak.MoveToCommand;
import de.fuzzlemann.ucutils.commands.time.ClockCommand;
import de.fuzzlemann.ucutils.commands.time.StopWatchCommand;
import de.fuzzlemann.ucutils.commands.time.TimerCommand;
import de.fuzzlemann.ucutils.commands.todo.*;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
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

    public static void registerAllCommands() {
        registerCommand(new TestCommand());

        registerCommand(new ClearChatCommand());
        registerCommand(new InternetTestCommand());
        registerCommand(new RefreshDataCommand());
        registerCommand(new CalculateCommand());

        registerCommand(new StopWatchCommand());
        registerCommand(new ClockCommand());

        TimerCommand timerCommand = new TimerCommand();
        registerCommand(timerCommand, timerCommand);

        registerCommand(new ASMSCommand());
        registerCommand(new ACallCommand());

        registerCommand(new ReplyCommand());
        registerCommand(new MobileBlockCommand());
        registerCommand(new MobileBlockListCommand());
        registerCommand(new DoNotDisturbCommand());

        registerCommand(new ToDoListCommand());
        registerCommand(new AddToDoCommand());
        registerCommand(new DoneToDoCommand());
        registerCommand(new RemoveToDoCommand());
        registerCommand(new ModifyToDoCommand());

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
        registerCommand(new FShutdownCommand());

        registerCommand(new CheckActiveMembersCommand());
        registerCommand(new ChannelActivityCommand());
        registerCommand(new CallReinforcementCommand());

        registerCommand(new ToggleMafiaSpeechCommand());
        registerCommand(new ToggleKerzakovSpeechCommand());
        registerCommand(new ToggleTriadenSpeechCommand());

        registerCommand(new SchwarzmarktLocationsCommand());

        registerCommand(new CheckMedicalLicenseCommand());

        registerCommand(new MoveHereCommand());
        registerCommand(new MoveCommand());
        registerCommand(new MoveToCommand());

        ASUCommand asuCommand = new ASUCommand();
        registerCommand(asuCommand, asuCommand);

        ModifyWantedsCommand modifyWantedsCommand = new ModifyWantedsCommand();
        registerCommand(modifyWantedsCommand, modifyWantedsCommand);

        ASellDrugCommand aSellDrugCommand = new ASellDrugCommand();
        registerCommand(aSellDrugCommand, aSellDrugCommand);

        DrugPriceCommand drugPriceCommand = new DrugPriceCommand();
        registerCommand(drugPriceCommand, drugPriceCommand);

        GiveDrugCommand giveDrugCommand = new GiveDrugCommand();
        registerCommand(giveDrugCommand, giveDrugCommand);

        ASetBlacklistCommand aSetBlacklistCommand = new ASetBlacklistCommand();
        registerCommand(aSetBlacklistCommand, aSetBlacklistCommand);

        BlacklistPriceCommand blacklistPriceCommand = new BlacklistPriceCommand();
        registerCommand(blacklistPriceCommand, blacklistPriceCommand);

        PunishCommand punishCommand = new PunishCommand();
        registerCommand(punishCommand, punishCommand);

        SendNoobChatCommand sendNoobChatCommand = new SendNoobChatCommand();
        registerCommand(sendNoobChatCommand, sendNoobChatCommand);

        registerCommand(new ABuyCommand());
    }

    private static void registerCommand(CommandExecutor commandExecutor) {
        registerCommand(commandExecutor, null);
    }

    private static void registerCommand(CommandExecutor commandExecutor, @Nullable TabCompletion tabCompletion) {
        Command commandAnnotation = getCommand(commandExecutor);

        String[] labels = commandAnnotation.labels();

        for (String label : labels) {
            ClientCommandHandler.instance.registerCommand(new BaseCommand(label, tabCompletion));
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
