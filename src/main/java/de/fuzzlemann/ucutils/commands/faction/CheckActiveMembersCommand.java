package de.fuzzlemann.ucutils.commands.faction;

import com.google.common.util.concurrent.Uninterruptibles;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class CheckActiveMembersCommand {

    private static final Timer TIMER = new Timer();
    private static final Pattern FACTION_MEMBERS_PATTERN = Pattern.compile("^ {2}=== Fraktionsmitglieder .+ ===$");
    private static final Map<Boolean, Integer> MEMBER_MAP = new HashMap<>();
    private static long memberlistShown;
    private static CompletableFuture<Map<Boolean, Integer>> future;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (future == null) return;

        String message = e.getMessage().getUnformattedText();
        long currentTime = System.currentTimeMillis();

        if (FACTION_MEMBERS_PATTERN.matcher(message).find()) {
            e.setCanceled(true);
            memberlistShown = currentTime;
            MEMBER_MAP.put(false, 0);
            MEMBER_MAP.put(true, 0);

            TIMER.schedule(new TimerTask() {
                @Override
                public void run() {
                    future.complete(MEMBER_MAP);
                }
            }, 300L);
            return;
        }

        if (currentTime - memberlistShown > 200L || !message.startsWith(" » ")) return;

        boolean inactive = !message.endsWith("AFK") && !message.endsWith("Nicht im Dienst");
        MEMBER_MAP.merge(inactive, 1, Integer::sum);

        e.setCanceled(true);
    }

    @Command(value = "checkactivemembers", async = true)
    public boolean onCommand() {
        Message.builder()
                .of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Aktive Spieler in den Fraktionen\n").color(TextFormatting.DARK_AQUA).advance()
                .joiner(Faction.values())
                .consumer((b, faction) -> {
                    Map<Boolean, Integer> members = getMembers(faction);
                    int activeMembers = members.get(true);

                    b.of(" * ").color(TextFormatting.DARK_GRAY).advance()
                            .of(faction.getFactionInfo().getFullName() + ": ").color(TextFormatting.GRAY)
                            .clickEvent(ClickEvent.Action.RUN_COMMAND, "/memberinfo " + faction.getFactionKey())
                            .hoverEvent(HoverEvent.Action.SHOW_TEXT, Message.builder().of("/memberinfo").color(TextFormatting.AQUA).advance().of(" ausführen").color(TextFormatting.GRAY).advance().build()).advance()
                            .of(String.valueOf(activeMembers)).color(TextFormatting.DARK_GREEN).advance()
                            .of("/").color(TextFormatting.GRAY).advance()
                            .of(String.valueOf(members.size())).color(TextFormatting.GREEN).advance();
                }).newLineJoiner().advance()
                .send();

        return true;
    }

    private Map<Boolean, Integer> getMembers(Faction faction) {
        future = new CompletableFuture<>();
        AbstractionLayer.getPlayer().sendChatMessage("/memberinfo " + faction.getFactionKey());

        try {
            return Uninterruptibles.getUninterruptibly(future);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        } finally {
            future = null;
        }
    }
}
