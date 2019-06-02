package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import de.fuzzlemann.ucutils.utils.sound.SoundUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class NotificationEventHandler {

    private static final Timer TIMER = new Timer();
    private static final Pattern RESOURCEPACK_PATTERN = Pattern.compile("^Wir empfehlen dir unser Resourcepack zu nutzen.$|" +
            "^Unter http://unicacity.de/dl/UnicaCity[_a-zA-Z\\d]+.zip kannst du es dir herunterladen.$");
    private static final Pattern UNINVITE_PATTERN = Pattern.compile("^(?:\\[UC])*[a-zA-Z0-9_]+ wurde von (?:\\[UC])*[a-zA-Z0-9_]+ aus der Fraktion geschmissen.$");
    private static final Pattern INVITE_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+ ist der Fraktion mit Rang \\d beigetreten.$");
    private static final Pattern FRIEND_JOINED_PATTERN = Pattern.compile("^ » Freundesliste: ([a-zA-Z0-9_]+) ist nun online.$");
    private static final Pattern REPORT_RECEIVED_PATTERN = Pattern.compile("^§cEs liegt ein neuer Report §8\\[§9\\d+§8]§c von §6[a-zA-Z0-9_]+ §cvor! Thema: §9[a-zA-Z]+$|" +
            "^Es liegt ein neuer Report von [a-zA-Z0-9_]+ vor! Thema: [a-zA-Z]+$");
    private static final Pattern REPORT_ACCEPTED_PATTERN = Pattern.compile("^\\[Report] Du hast den Report von [a-zA-Z0-9_]+ \\[Level \\d+] angenommen! Thema: [a-zA-Z]+$");
    private static final Pattern BOMB_PLACED_PATTERN = Pattern.compile("^News: ACHTUNG! Es wurde eine Bombe in der Nähe von .+ gefunden!$");
    private static final Pattern SERVICE_ANNOUNCEMENT_PATTERN = Pattern.compile("^HQ: Achtung! Ein Notruf von (?:\\[UC])*[a-zA-Z0-9_]+ \\(.+\\): \".+\"$|" +
            "^Ein Notruf von (?:\\[UC])*[a-zA-Z0-9_]+ \\(.+\\): \".+\"$");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedText = message.getUnformattedText();

        Matcher friendJoinedMatcher = FRIEND_JOINED_PATTERN.matcher(unformattedText);
        if (friendJoinedMatcher.find()) {
            String friendName = friendJoinedMatcher.group(1);

            modifyFriendJoin(message, friendName);
            return;
        }

        if (ConfigUtil.blockResourcePackReminder && RESOURCEPACK_PATTERN.matcher(unformattedText).find()) {
            e.setCanceled(true);
            return;
        }

        EntityPlayerSP p = Main.MINECRAFT.player;

        if (ConfigUtil.inviteAnnouncement) {
            if (INVITE_PATTERN.matcher(unformattedText).find()) {
                p.playSound(SoundUtil.PLAYER_INVITED, 1, 1);
                return;
            } else if (UNINVITE_PATTERN.matcher(unformattedText).find()) {
                p.playSound(SoundUtil.PLAYER_UNINVITED, 1, 1);
                return;
            }
        }

        if (ConfigUtil.reportAnnouncement && REPORT_RECEIVED_PATTERN.matcher(unformattedText).find()) {
            p.playSound(SoundUtil.REPORT_RECEIVED, 3, 1);
            return;
        }

        if (ConfigUtil.bombAnnouncement && BOMB_PLACED_PATTERN.matcher(unformattedText).find()) {
            p.playSound(SoundUtil.BOMB_PLACED, 0.15F, 1);
            return;
        }

        //TODO REGEX
        if (ConfigUtil.contractFulfilledAnnouncement
                && unformattedText.startsWith("[Contract] ")
                && unformattedText.contains(" getötet. Kopfgeld: ")) {
            p.playSound(SoundUtil.CONTRACT_FULFILLED, 1, 1);
            return;
        }

        //TODO REGEX
        if (ConfigUtil.contractAnnouncement && unformattedText.startsWith("[Contract] Es wurde ein Kopfgeld auf")) {
            p.playSound(SoundUtil.CONTRACT_PLACED, 1, 1);
            return;
        }

        if (ConfigUtil.serviceAnnouncement && SERVICE_ANNOUNCEMENT_PATTERN.matcher(unformattedText).find()) {
            p.playSound(SoundUtil.SERVICE_RECEIVED, 1, 1);
            return;
        }

        if (!ConfigUtil.reportGreeting.isEmpty() && REPORT_ACCEPTED_PATTERN.matcher(unformattedText).find()) {
            TIMER.schedule(new TimerTask() {
                @Override
                public void run() {
                    p.sendChatMessage(ConfigUtil.reportGreeting);
                }
            }, 1000L);
        }
    }

    private static void modifyFriendJoin(ITextComponent message, String friendName) {
        ITextComponent buttons = Message.builder()
                .space()
                .of("[☎]").color(TextFormatting.DARK_GREEN)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Rufe " + friendName + " an", TextFormatting.DARK_GREEN))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/acall " + friendName).advance()
                .space()
                .of("[✉]").color(TextFormatting.GREEN)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Schreibe eine SMS an " + friendName, TextFormatting.GREEN))
                .clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/asms " + friendName + " ").advance()
                .space()
                .of("[✗]").color(TextFormatting.RED)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Lösche " + friendName + " als Freund", TextFormatting.RED))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/friend delete " + friendName).advance()
                .build().toTextComponent();

        message.appendSibling(buttons);
    }
}
