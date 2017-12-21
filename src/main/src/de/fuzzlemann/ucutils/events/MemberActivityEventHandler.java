package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.text.Message;
import lombok.val;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class MemberActivityEventHandler {

    public static final List<String> MEMBER_LIST = new ArrayList<>();

    private static final Timer TIMER = new Timer();
    private static long lastMessage;
    private static long shown;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        val message = e.getMessage();
        val unformattedMessage = message.getUnformattedText();

        long currentTime = System.currentTimeMillis();
        if (unformattedMessage.startsWith("Member Aktivit\u00e4t der Fraktion: ")) {
            MEMBER_LIST.clear();
            shown = currentTime;
            return;
        }

        if (currentTime - shown > 2000L || !unformattedMessage.startsWith("  - ") || !unformattedMessage.contains("("))
            return;

        lastMessage = currentTime;
        String name = unformattedMessage.substring(4, unformattedMessage.length() - 1);
        name = name.split(":")[0];

        MEMBER_LIST.add(name);

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                if (lastMessage <= 400L) return;

                Main.MINECRAFT.player.sendMessage(Message.builder().of("\u00bb ").color(TextFormatting.GOLD).advance()
                        .of("Memberanzahl: ").color(TextFormatting.GRAY).advance()
                        .of(String.valueOf(MEMBER_LIST.size())).color(TextFormatting.DARK_GRAY).advance().build().toTextComponent());

                lastMessage = 0;
                shown = 0;
            }
        }, 500L);
    }
}
