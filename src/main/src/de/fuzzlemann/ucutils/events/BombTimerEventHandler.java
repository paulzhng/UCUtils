package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class BombTimerEventHandler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss");
    private static final Pattern BOMB_PLACED_PATTERN = Pattern.compile("^News: ACHTUNG! Es wurde eine Bombe in der Nähe von .+ gefunden!$");
    private static final Pattern BOMB_REMOVED_PATTERN = Pattern.compile("News: Die Bombe konnte (?:nicht|erfolgreich) entschärft werden!");
    public static long bombPlaced = -1;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (!ConfigUtil.bombTimerDisplay) return;

        String message = e.getMessage().getUnformattedText();

        if (BOMB_PLACED_PATTERN.matcher(message).find()) {
            bombPlaced = System.currentTimeMillis();
            return;
        }

        if (BOMB_REMOVED_PATTERN.matcher(message).find()) {
            bombPlaced = -1;
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.RenderTickEvent e) {
        if (!ConfigUtil.bombTimerDisplay) return;

        long timeDifference = System.currentTimeMillis() - bombPlaced;

        if (timeDifference == -1) return;
        if (timeDifference > TimeUnit.MINUTES.toMillis(15)) {
            bombPlaced = -1;
            return;
        }

        Minecraft minecraft = Main.MINECRAFT;
        ScaledResolution res = new ScaledResolution(minecraft);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();

        FontRenderer fontRender = Main.MINECRAFT.fontRenderer;
        minecraft.entityRenderer.setupOverlayRendering();

        String timeString = formatTime(timeDifference);

        int titleWidth = fontRender.getStringWidth("Bomben-Timer");
        int timeWidth = fontRender.getStringWidth(timeString);

        int x = (width / 2);

        fontRender.drawStringWithShadow("Bomben-Timer", (float) (x - titleWidth / 2.0), (int) (height * 0.05 - 10), 0x009999);
        fontRender.drawStringWithShadow(timeString, (int) (x - timeWidth / 2.0), (int) (height * 0.05), 0x990000);
    }
    private static String formatTime(long time) {
        return DATE_FORMAT.format(new Date(time));
    }
}
