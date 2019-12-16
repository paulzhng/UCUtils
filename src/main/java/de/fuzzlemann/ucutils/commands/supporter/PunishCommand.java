package de.fuzzlemann.ucutils.commands.supporter;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.common.udf.data.supporter.violation.Violation;
import de.fuzzlemann.ucutils.utils.punishment.PunishManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class PunishCommand implements TabCompletion {

    @Command(value = "punish", usage = "/%label% [Spieler] [Grund...]")
    public boolean onCommand(UPlayer p, String target, @CommandParam(arrayStart = true) Violation[] violations) {
        Violation violation = PunishManager.combineViolations(violations);
        for (String commands : violation.getCommands(target)) {
            p.sendChatMessage(commands);
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.emptyList();

        return PunishManager.getViolations();
    }
}