package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.command.api.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class ASetBlacklistCommand implements TabCompletion {

    @Command(value = {"asetblacklist", "asetbl"}, usage = "/%label% [Spieler...] [Grund]")
    public boolean onCommand(UPlayer p, @CommandParam(arrayStart = true) String[] targets, BlacklistReason reason) {
        for (String player : targets) {
            p.sendChatMessage("/bl set " + player + " 100 " + reason.getPrice() + " " + reason.getReason());
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length != 2) return Collections.emptyList();

        return BlacklistUtil.BLACKLIST_REASONS
                .stream()
                .map(BlacklistReason::getReason)
                .collect(Collectors.toList());
    }
}
