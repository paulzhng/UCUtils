package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class ASetBlacklistCommand implements TabCompletion {

    private final Timer timer = new Timer();

    @Command(value = {"asetblacklist", "asetbl"}, usage = "/%label% [Spieler...] [Grund]")
    public boolean onCommand(UPlayer p, @CommandParam(arrayStart = true) String[] targets, BlacklistReason reason) {
        if (targets.length < 14) {
            for (String player : targets) {
                p.sendChatMessage("/bl set " + player + " " + reason.getKills() + " " + reason.getAmount() + " " + reason.getReason());
            }
        } else {
            timer.scheduleAtFixedRate(new TimerTask() {
                private int i;
                @Override
                public void run() {
                    if (i >= targets.length) {
                        cancel();
                        return;
                    }

                    String player = targets[i++];

                    p.sendChatMessage("/bl set " + player + " " + reason.getKills() + " " + reason.getAmount() + " " + reason.getReason());
                }
            }, 0, TimeUnit.SECONDS.toMillis(1));
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.emptyList();

        List<String> completions = BlacklistUtil.BLACKLIST_REASONS
                .stream()
                .map(BlacklistReason::getReason)
                .collect(Collectors.toList());
        String input = args[args.length - 1].toLowerCase().replace('-', ' ');

        completions.removeIf(tabComplete -> !tabComplete.toLowerCase().startsWith(input));

        completions.addAll(ForgeUtils.getOnlinePlayers());

        return completions;
    }
}
