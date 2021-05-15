package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReasons;
import de.fuzzlemann.ucutils.events.NameFormatEventHandler;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author Dimikou
 */
@Mod.EventBusSubscriber
public class ModifyBlacklistCommand implements TabCompletion {

    private static String target;
    private static ModifyBlacklistType type;
    private static BlacklistReason addReason;
    private static long executedTime = -1;

    @Command(value = {"modifyblacklist", "mbl"}, usage = "/%label% [Spieler] [Grund/-v]")
    public boolean onCommand(UPlayer p, String target,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) BlacklistReason reason,
                             @CommandParam(required = false, requiredValue = "-v") boolean outlaw) {
        if (!outlaw && reason == null) return false; // we need one of both
        if (outlaw && reason != null) return false; // but not both at the same time

        ModifyBlacklistCommand.target = target;
        if (reason != null) {
            addReason = reason;
            type = ModifyBlacklistType.MODIFY_REASON;
        } else {
            type = ModifyBlacklistType.OUTLAW;
        }

        executedTime = System.currentTimeMillis();

        p.sendChatMessage("/bl");
        return true;
    }

    @SubscribeEvent
    public static void onBlacklistInfo(ClientChatReceivedEvent e) {
        if (System.currentTimeMillis() - executedTime > 1000L) return;

        String text = e.getMessage().getUnformattedText();

        // remove start message
        Matcher startPattern = NameFormatEventHandler.BLACKLIST_START_PATTERN.matcher(text);
        if (startPattern.find()) {
            e.setCanceled(true);
            return;
        }

        Matcher listPattern = NameFormatEventHandler.BLACKLIST_LIST_PATTERN.matcher(text);
        if (!listPattern.find()) return;

        // remove list message
        e.setCanceled(true);

        // extract variables
        String name = listPattern.group(1);
        String reason = listPattern.group(2);
        // issuer (group 3) is ignored
        int kills = Integer.parseInt(listPattern.group(4));
        int price = Integer.parseInt(listPattern.group(5));

        if (!name.equals(target)) return;

        if (type == ModifyBlacklistType.OUTLAW) {
            if (reason.contains("[Vogelfrei]")) {
                TextUtils.error("Der Spieler ist bereits Vogelfrei.");
                return;
            }
            
            reason = removeModifiers(reason);
            reason += " [Vogelfrei]"; // append outlaw reason
        } else {
            if (reason.contains(addReason.getReason())) {
                TextUtils.error("Der Spieler besitzt diesen Blacklistgrund bereits.");
                return;
            }

            kills = Math.min(kills + addReason.getKills(), 100); // max 100 kills
            price = Math.min(price + addReason.getAmount(), 10000); // max 10.000$ bounty
            if (addReason.getReason().startsWith("[") || addReason.getReason().startsWith("(")) {
                reason = removeModifiers(reason);
                reason = reason + " " + addReason.getReason(); // append modifier to blacklist
            } else {
                reason = addReason.getReason() + " + " + reason; // prepend reason to original one
            }

        }

        // delete from and re-add blacklist
        UPlayer p = AbstractionLayer.getPlayer();
        p.sendChatMessage("/bl del " + target);
        p.sendChatMessage("/bl set " + target + " " + kills + " " + price + " " + reason);
    }

    //Removes all known Modifiers
    private static String removeModifiers(String reason) {
        reason = reason.replace(" [Yobannoe dno]", "") // Removes Yobbanoe dno modifier
        .replace(" (muerte)", "") // Removes Muerte modifier
        .replace(" [Vogelfrei]", ""); // Removes Vogelfrei modifier

        return reason;
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

        return completions;
    }

    enum ModifyBlacklistType {
        MODIFY_REASON,
        OUTLAW
    }
}
