package de.fuzzlemann.ucutils.commands.supporter;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.punishment.PunishManager;
import de.fuzzlemann.ucutils.utils.punishment.Violation;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class PunishCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(value = "punish", usage = "/%label% [Spieler] [Grund...]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        List<Violation> violations = getViolations(args);
        if (violations.isEmpty()) return false;

        Violation violation = Violation.combineViolations(violations);
        for (String commands : violation.getCommands(args[0])) {
            p.sendChatMessage(commands);
        }

        return true;
    }

    private List<Violation> getViolations(String[] args) {
        Set<Violation> violations = new HashSet<>();

        for (int i = 1; i < args.length; i++) {
            Violation violation = PunishManager.getViolation(args[i].replace('-', ' '));
            if (violation != null)
                violations.add(violation);
        }

        return new ArrayList<>(violations);
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length == 1) return Collections.emptyList();

        return PunishManager.getViolations();
    }
}