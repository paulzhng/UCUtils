package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.keybind.KeyBindRegistry;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class AdControlEventHandler {

    private static final Pattern AD_CONTROL_PATTERN = Pattern.compile("^\\[Werbung] ([a-zA-Z0-9_]+) hat eine Werbung geschalten: .+$");
    private static long adTime;
    private static String adSender;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        String message = e.getMessage().getUnformattedText();
        Matcher matcher = AD_CONTROL_PATTERN.matcher(message);
        if (!matcher.find()) return;

        adSender = matcher.group(1);
        adTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public static void onKeyboardClickEvent(InputEvent.KeyInputEvent e) {
        if (!Main.MINECRAFT.inGameHasFocus) return;
        if (adSender == null) return;
        if (System.currentTimeMillis() - adTime > TimeUnit.SECONDS.toMillis(20)) return;

        UPlayer p = AbstractionLayer.getPlayer();
        if (Keyboard.isKeyDown(KeyBindRegistry.acceptAD.getKeyCode())) {
            p.sendChatMessage("/adcontrol " + adSender + " freigeben");
            adSender = null;
        } else if (Keyboard.isKeyDown(KeyBindRegistry.denyAD.getKeyCode())) {
            p.sendChatMessage("/adcontrol " + adSender + " blockieren");
            adSender = null;
        }
    }
}

