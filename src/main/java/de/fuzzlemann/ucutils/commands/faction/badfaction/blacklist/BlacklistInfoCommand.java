package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.events.NameFormatEventHandler;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.regex.Matcher;

@Mod.EventBusSubscriber
public class BlacklistInfoCommand {

    private final static Set<String> PLAYERS = new HashSet<>();
    private static long executedTime = -1;

    @Command(value = {"blacklistinfo", "blinfo"}, usage = "/%label% [Spieler...]")
    public boolean onCommand(UPlayer p, @CommandParam(arrayStart = true) String[] targets) {
        PLAYERS.clear();

        executedTime = System.currentTimeMillis();

        List<String> onlinePlayers = ForgeUtils.getOnlinePlayers();

        Arrays.stream(targets)
                .map(target -> ForgeUtils.getMostMatching(onlinePlayers, target))
                .filter(Objects::nonNull)
                .forEach(PLAYERS::add);

        p.sendChatMessage("/bl");
        return true;
    }

    @SubscribeEvent
    public void onBlacklistInfo(ClientChatReceivedEvent e) {
        if (System.currentTimeMillis() - executedTime > 1000L) return;

        String text = e.getMessage().getUnformattedText();
        Matcher matcher = NameFormatEventHandler.BLACKLIST_LIST_PATTERN.matcher(text);
        if (!matcher.find()) return;

        String name = matcher.group(0);
        if (!PLAYERS.contains(name))
            e.setCanceled(true);
    }
}
