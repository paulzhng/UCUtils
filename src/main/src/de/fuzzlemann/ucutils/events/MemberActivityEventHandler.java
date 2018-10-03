package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.ITextComponent;
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
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class MemberActivityEventHandler {

    public static final List<String> MEMBER_LIST = new ArrayList<>();

    private static final Timer TIMER = new Timer();
    private static final Pattern MEMBER_ACTIVITY_PATTERN = Pattern.compile("^Member Aktivit\u00e4t der Fraktion: .+$");
    private static long lastMessage;
    private static long shown;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedMessage = message.getUnformattedText();

        long currentTime = System.currentTimeMillis();
        if (MEMBER_ACTIVITY_PATTERN.matcher(unformattedMessage).find()) {
            MEMBER_LIST.clear();
            shown = currentTime;
            return;
        }

        if (currentTime - shown > 500L || !unformattedMessage.startsWith("  - ") || !unformattedMessage.contains("("))
            return;

        lastMessage = currentTime;

        //TODO REGEX
        String name = unformattedMessage.substring(4, unformattedMessage.length() - 1);
        name = name.split(":")[0];

        MEMBER_LIST.add(name);

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                if (lastMessage <= 50L) return;

                Main.MINECRAFT.player.sendMessage(Message.builder().of("\u00bb ").color(TextFormatting.GRAY).advance()
                        .of("Memberanzahl: ").color(TextFormatting.DARK_AQUA).advance()
                        .of(String.valueOf(MEMBER_LIST.size())).color(TextFormatting.GREEN).advance().build().toTextComponent());

                lastMessage = 0;
                shown = 0;
            }
        }, 100L);
    }
}
