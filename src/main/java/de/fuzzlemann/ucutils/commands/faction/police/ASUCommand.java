package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.faction.police.WantedReason;
import de.fuzzlemann.ucutils.utils.math.Expression;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASUCommand implements TabCompletion {

    @Command(value = "asu", usage = "/%label% [Spieler...] [Grund] (Variation) (-v/-b/-fsa)")
    public boolean onCommand(UPlayer p, String[] args) {
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

    private void giveWanteds(UPlayer issuer, String reason, int amount, List<String> players) {
        amount = Math.min(amount, 69);

        for (String player : players) {
            issuer.sendChatMessage("/su " + amount + " " + player + " " + reason);
        }
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.emptyList();

        List<String> completions = WantedManager.getWantedReasons();
        String input = args[args.length - 1].toLowerCase().replace('-', ' ');

        completions.removeIf(tabComplete -> !tabComplete.toLowerCase().startsWith(input));

        completions.addAll(ForgeUtils.getOnlinePlayers());

        return completions;
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
