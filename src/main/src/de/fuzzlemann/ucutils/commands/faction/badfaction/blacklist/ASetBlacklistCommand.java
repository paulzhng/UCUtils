package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class ASetBlacklistCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(labels = {"asetblacklist", "asetbl"}, usage = "/%label% [Spieler(...)] [Grund]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        int reasonIndex = args.length - 1;
        List<String> players = Arrays.asList(args).subList(0, reasonIndex);
        String reason = args[reasonIndex];

        BlacklistReason blacklistReason = BlacklistUtil.getBlacklistReason(reason);

        if (blacklistReason == null) {
            TextUtils.error("Der Blacklistgrund wurde nicht gefunden.");
            return true;
        }

        giveWanteds(p, blacklistReason.getReason(), players);
        return true;
    }

    private void giveWanteds(EntityPlayerSP issuer, String reason, List<String> players) {
        for (String player : players) {
            issuer.sendChatMessage("/bl set " + player + " " + reason);
        }
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Collections.emptyList();

        String reason = args[args.length - 1].toLowerCase();
        List<String> blacklistReasons = BlacklistUtil.BLACKLIST_REASONS
                .stream()
                .map(BlacklistReason::getReason)
                .map(blacklistReason -> blacklistReason.replace(' ', '-'))
                .collect(Collectors.toList());

        if (reason.isEmpty()) return blacklistReasons;

        blacklistReasons.removeIf(blacklistReason -> !blacklistReason.toLowerCase().startsWith(reason));
        return blacklistReasons;
    }
}
