package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
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
public class CheckActiveMembersCommand implements CommandExecutor {

    private static final Timer TIMER = new Timer();
    private static final Pattern FACTION_MEMBERS_PATTERN = Pattern.compile("^ {2}=== Fraktionsmitglieder .+ ===$");
    private static long memberlistShown;
    private static final Map<Boolean, Integer> MEMBER_MAP = new HashMap<>();
    private static CompletableFuture<Map<Boolean, Integer>> future;

    @Override
    @Command(labels = "checkactivemembers")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        new Thread(() -> {
            Message.MessageBuilder builder = Message.builder();

            builder.of("» ").color(TextFormatting.GOLD).advance();
            builder.of("Aktive Spieler in den Fraktionen\n").color(TextFormatting.DARK_PURPLE).advance();

            for (Faction faction : Faction.values()) {
                Map<Boolean, Integer> members = getMembers(faction);

                int activeMembers = members.get(true);
                int inactiveMembers = members.get(false);

                builder.of(" * ").color(TextFormatting.GOLD).advance();
                builder.of(faction.getFactionInfo().getFullName() + ": ")
                        .color(TextFormatting.GRAY).advance();

                builder.of(String.valueOf(activeMembers)).color(TextFormatting.DARK_GREEN).advance();
                builder.of("/").color(TextFormatting.GRAY).advance();
                builder.of((inactiveMembers + activeMembers) + "\n").color(TextFormatting.GREEN).advance();
            }

            p.sendMessage(builder.build().toTextComponent());
        }).start();
        return true;
    }

    private Map<Boolean, Integer> getMembers(Faction faction) {
        future = new CompletableFuture<>();
        Main.MINECRAFT.player.sendChatMessage("/memberinfo " + faction.getFactionKey());

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } finally {
            future = null;
        }
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (future == null) return;

        String message = e.getMessage().getUnformattedText();
        long currentTime = System.currentTimeMillis();

        if (FACTION_MEMBERS_PATTERN.matcher(message).find()) {
            e.setCanceled(true);
            memberlistShown = currentTime;
            MEMBER_MAP.clear();
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
        MEMBER_MAP.merge(inactive, 1, (a, b) -> a + b);

        e.setCanceled(true);
    }
}
