package de.fuzzlemann.ucutils.utils.mobile;

import de.fuzzlemann.ucutils.utils.io.JsonManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class MobileUtils {

    private static final File BLOCKED_FILE = new File(JsonManager.DIRECTORY, "blocked.storage");
    private static final List<String> BLOCKED_PLAYERS = new ArrayList<>(JsonManager.loadObjects(BLOCKED_FILE, String.class));
    private static final Pattern SMS_PATTERN = Pattern.compile("^Dein Handy klingelt! Eine Nachricht von ([a-zA-Z0-9_]+) \\((\\d+)\\)\\.$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^Nummer von [a-zA-Z0-9_]+: (\\d+)$");

    private static boolean blockNextMessage;
    private static CompletableFuture<Integer> future;
    private static int lastNumber = -1;

    public static void block(String playerName) {
        BLOCKED_PLAYERS.add(playerName);
        save();
    }

    public static void unblock(String playerName) {
        BLOCKED_PLAYERS.remove(playerName);
        save();
    }

    public static boolean isBlocked(String playerName) {
        return BLOCKED_PLAYERS.contains(playerName);
    }

    public static List<String> getBlockedPlayers() {
        return BLOCKED_PLAYERS;
    }

    private static void save() {
        new Thread(() -> JsonManager.writeList(BLOCKED_FILE, BLOCKED_PLAYERS)).start();
    }

    public static int getNumber(EntityPlayerSP p, String numberPlayer) {
        future = new CompletableFuture<>();
        p.sendChatMessage("/nummer " + numberPlayer);

        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    public static int getLastNumber() {
        return lastNumber;
    }

    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent e) {
        if (blockNextMessage) {
            e.setCanceled(true);
            blockNextMessage = false;
            return;
        }

        String message = e.getMessage().getUnformattedText();

        Matcher smsMatcher = SMS_PATTERN.matcher(message);
        if (smsMatcher.find()) {
            String playerName = smsMatcher.group(1);

            if (MobileUtils.isBlocked(playerName)) {
                e.setCanceled(true);
                blockNextMessage = true;
                return;
            }

            lastNumber = Integer.parseInt(smsMatcher.group(2));
        }

        if (future != null) {
            if (message.equals("Spieler nicht gefunden.")) {
                future.complete(-1);
                future = null;
                return;
            }

            Matcher numberMatcher = NUMBER_PATTERN.matcher(message);
            if (!numberMatcher.find()) return;

            int number = Integer.parseInt(numberMatcher.group(1));

            future.complete(number);
            future = null;

            e.setCanceled(true);
        }
    }
}
