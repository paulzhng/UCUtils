package de.fuzzlemann.ucutils.utils.faction.police;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.events.NameFormatEventHandler;
import de.fuzzlemann.ucutils.utils.io.JsonManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class WantedManager {

    private static final File WANTED_FILE = new File(JsonManager.DIRECTORY, "wanteds.storage");
    private static final List<WantedReason> WANTED_LIST = new ArrayList<>();

    private static final Pattern WANTED_INFO_PATTERN = Pattern.compile("^HQ: [a-zA-Z0-9_]+ wird aus folgendem Grund gesucht: .+, Over.$");

    private static boolean blockNextMessage;
    private static CompletableFuture<Wanted> future;

    public static void fillWantedList() throws IOException {
        URL url = new URL("http://fuzzlemann.de/wanteds.html");
        String result = IOUtils.toString(url, StandardCharsets.UTF_8);

        String[] wantedStrings = result.split("<>");

        for (String wantedString : wantedStrings) {
            String[] splittedWantedString = wantedString.split(";");

            String reason = StringEscapeUtils.unescapeJava(splittedWantedString[0]);
            int wanteds = Integer.parseInt(splittedWantedString[1]);

            WANTED_LIST.add(new WantedReason(reason, wanteds));
        }

        JsonManager.writeList(WANTED_FILE, WANTED_LIST);
    }

    public static void readSavedWantedList() {
        WANTED_LIST.addAll(JsonManager.loadObjects(WANTED_FILE, WantedReason.class)
                .stream()
                .map(object -> (WantedReason) object)
                .collect(Collectors.toList()));
    }

    public static List<String> getWantedReasons() {
        return WANTED_LIST.stream()
                .map(WantedReason::getReason)
                .collect(Collectors.toList());
    }

    public static WantedReason getWantedReason(String reason) {
        for (WantedReason wanted : WANTED_LIST) {
            if (wanted.getReason().equalsIgnoreCase(reason)) return wanted;
        }

        WantedReason foundWanted = null;
        String lowerReason = reason.toLowerCase();

        int delta = Integer.MAX_VALUE;

        for (WantedReason wanted : WANTED_LIST) {
            String wantedReason = wanted.getReason().toLowerCase();
            if (!wantedReason.startsWith(lowerReason)) continue;

            int curDelta = Math.abs(wantedReason.length() - lowerReason.length());
            if (curDelta < delta) {
                foundWanted = wanted;
                delta = curDelta;
            }

            if (curDelta == 0) break;
        }

        return foundWanted;
    }

    public static Future<Wanted> getWanteds(String player) {
        future = new CompletableFuture<>();
        Main.MINECRAFT.player.sendChatMessage("/wantedinfo " + player);
        return future;
    }

    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent e) {
        if (blockNextMessage) {
            e.setCanceled(true);
            blockNextMessage = false;
            return;
        }

        String message = e.getMessage().getUnformattedText();

        if (future != null) {
            if (message.equals("HQ: Der Spieler wird nicht gesucht, Over.")) {
                future.complete(null);
                future = null;
                return;
            }

            if (!WANTED_INFO_PATTERN.matcher(message).find()) return;
            e.setCanceled(true);

            String[] splittedMessage = message.split(" ");

            String player = splittedMessage[1];
            Integer wanteds = NameFormatEventHandler.WANTED_MAP.get(player);

            if (wanteds == null) {
                blockNextMessage = true;
                future.complete(null);
                future = null;
                return;
            }

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < splittedMessage.length; i++) {
                if (i <= 6 || i == splittedMessage.length - 1) continue;

                String messagePart = splittedMessage[i];
                boolean lastString = i == splittedMessage.length - 2;

                if (lastString)
                    messagePart = messagePart.substring(0, messagePart.length() - 1);

                sb.append(messagePart);

                if (!lastString)
                    sb.append(" ");
            }

            future.complete(new Wanted(sb.toString(), wanteds));
            future = null;

            blockNextMessage = true;
        }
    }
}
