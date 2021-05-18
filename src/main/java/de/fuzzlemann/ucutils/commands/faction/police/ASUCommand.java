package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.faction.police.WantedReason;
import de.fuzzlemann.ucutils.utils.math.Expression;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASUCommand implements TabCompletion {

    private final Timer timer = new Timer();

    @Command(value = "asu", usage = "/%label% [Spieler...] [Grund] (-v/-b/-fsa/-wsa)")
    public boolean onCommand(UPlayer p, String[] args) {
        if (args.length < 2) return false;

        Set<Flag> flags = getFlags(args);

        int reasonIndex = args.length - flags.size() - 1;

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

        giveWanteds(p, wantedReason, wantedAmount, players);
        return true;
    }

    private void giveWanteds(UPlayer issuer, String reason, int amount, List<String> players) {
        int maxAmount = Math.min(amount, 69);

        if (players.size() > 14) {
            timer.scheduleAtFixedRate(new TimerTask() {
                private int i;

                @Override
                public void run() {
                    if (i >= players.size()) {
                        cancel();
                        return;
                    }

                    String player = players.get(i++);

                    issuer.sendChatMessage("/su " + maxAmount + " " + player + " " + reason);
                }
            }, 0, TimeUnit.SECONDS.toMillis(1));
        } else{
            for (String player : players) {
                issuer.sendChatMessage("/su " + amount + " " + player + " " + reason);
            }
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
        DRIVERS_LICENSE_WITHDRAWAL("-fsa", "", " + Führerscheinabnahme", "x"),
        WEAPONS_LICENSE_WITHDRAWAL("-wsa", "", "+ Waffenscheinabnahme", "x");

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
