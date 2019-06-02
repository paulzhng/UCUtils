package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.faction.police.WantedReason;
import de.fuzzlemann.ucutils.utils.math.Expression;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASUCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(labels = "asu", usage = "/%label% [Spieler...] [Grund] (Variation) (-v/-b/-fsa)")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        Set<Flag> flags = getFlags(args);
        int variationIndex = args.length - 1 - flags.size();

        int variation = 0;
        try {
            variation = Integer.parseInt(args[variationIndex]);
        } catch (NumberFormatException e) {
            variationIndex++;
        }

        if (Math.abs(variation) > 10) {
            TextUtils.error("Die Variation darf nicht größer als 10 Wanteds sein.");
            return true;
        }

        int reasonIndex = variationIndex - 1;

        List<String> players = Arrays.asList(args).subList(0, reasonIndex);
        String reason = args[reasonIndex];

        WantedReason wanted = WantedManager.getWantedReason(reason.replace('-', ' '));

        if (wanted == null) {
            TextUtils.error("Der Wantedgrund wurde nicht gefunden.");
            return true;
        }

        String wantedReason = wanted.getReason();
        int wantedAmount = wanted.getAmount();

        for (Flag flag : flags) {
            wantedReason = flag.modifyReason(wantedReason);
            wantedAmount = flag.modifyWanteds(wantedAmount);
        }

        giveWanteds(p, wantedReason, wantedAmount + variation, players);
        return true;
    }

    private void giveWanteds(EntityPlayerSP issuer, String reason, int amount, List<String> players) {
        amount = Math.min(amount, 69);

        for (String player : players) {
            issuer.sendChatMessage("/su " + amount + " " + player + " " + reason);
        }
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Collections.emptyList();

        return WantedManager.getWantedReasons();
    }

    private Set<Flag> getFlags(String[] args) {
        Set<Flag> flags = new HashSet<>();

        for (String arg : args) {
            Flag flag = Flag.getFlag(arg);

            if (flag != null)
                flags.add(flag);
        }

        return flags;
    }

    private enum Flag {
        TRIED("-v", "Versuchte/r/s ", "", "x/2"),
        SUBSIDY("-b", "Beihilfe bei der/dem ", "", "x-10"),
        DRIVERS_LICENSE_WITHDRAWAL("-fsa", "", " + Führerscheinabnahme", "x");

        private final String flagArgument;
        private final String prependReason;
        private final String postponeReason;
        private final String wantedModification;

        Flag(String flagArgument, String prependReason, String postponeReason, String wantedModification) {
            this.flagArgument = flagArgument;
            this.prependReason = prependReason;
            this.postponeReason = postponeReason;
            this.wantedModification = wantedModification;
        }

        static Flag getFlag(String string) {
            for (Flag flag : Flag.values()) {
                if (flag.flagArgument.equalsIgnoreCase(string)) return flag;
            }

            return null;
        }

        private String modifyReason(String reason) {
            return prependReason + reason + postponeReason;
        }

        private int modifyWanteds(int wanteds) {
            return (int) new Expression(wantedModification.replace("x", String.valueOf(wanteds))).evaluate();
        }
    }
}
