package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReasons;
import de.fuzzlemann.ucutils.events.NameFormatEventHandler;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class ModifyBlacklistCommand implements TabCompletion {

    private static UPlayer p;
    private static String target;
    private static BlacklistReason addReason;
    private static boolean vogelfrei = false;
    private static long executedTime = -1;

    @Command(value = {"modifyblacklist", "mbl"}, usage = "/%label% [Spieler] [Grund]")
    public boolean onCommand(UPlayer p, String target,
                             @CommandParam(required = false) BlacklistReason reason,
                             @CommandParam(required = false, requiredValue = "-v") boolean vogelfrei) {
        this.vogelfrei = vogelfrei;
        this.target = target;
        this.p = p;
        if (reason != null) {
            this.addReason = reason;
        }
        this.executedTime = System.currentTimeMillis();
        p.sendChatMessage("/bl");
        return true;
    }

    @SubscribeEvent
    public static void onBlacklistInfo(ClientChatReceivedEvent e) {
        if (System.currentTimeMillis() - executedTime > 1000L) return;

        String text = e.getMessage().getUnformattedText();
        Matcher matcher = NameFormatEventHandler.BLACKLIST_START_PATTERN.matcher(text);

        if (matcher.find()) {
            e.setCanceled(true);
        }
        matcher = NameFormatEventHandler.BLACKLIST_LIST_PATTERN.matcher(text);

        if (!matcher.find()) return;

        String name = matcher.group(1);
        if (name.equals(target)) {
            if (vogelfrei) {
                int kills = Integer.parseInt(matcher.group(4));
                int price = Integer.parseInt(matcher.group(5));
                String reason = matcher.group(2) + " [Vogelfrei]";
                p.sendChatMessage("/bl del " + target);
                p.sendChatMessage("/bl set " + target + " " + kills + " " + price + " " + reason);
                e.setCanceled(true);
                return;
            }
            int kills = calcKills(matcher);
            int price = calcPrice(matcher);
            String reasons = addReason.getReason() + " + " + matcher.group(2);
            p.sendChatMessage("/bl del " + target);
            p.sendChatMessage("/bl set " + target + " " + kills + " " + price + " " + reasons);
        }

        e.setCanceled(true);
    }

    private static int calcKills(Matcher matcher) {
        if (Integer.parseInt(matcher.group(4)) + addReason.getKills() > 100) return 100;

        return Integer.parseInt(matcher.group(4)) + addReason.getKills();
    }

    private static int calcPrice(Matcher matcher) {

        if (Integer.parseInt(matcher.group(5)) + addReason.getAmount() > 10000) return 10000;

        return Integer.parseInt(matcher.group(5)) + addReason.getAmount();
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