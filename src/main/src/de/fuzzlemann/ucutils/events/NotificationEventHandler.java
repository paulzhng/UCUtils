package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.SoundUtil;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import lombok.val;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class NotificationEventHandler {

    private static final Pattern RESOURCEPACK_PATTERN = Pattern.compile("^Wir empfehlen dir unser Resourcepack zu nutzen.$|" +
            "^Unter http://server.unicacity.de/dl/UnicaCity_v2R7.zip kannst du es dir herunterladen.$");
    private static final Pattern UNINVITE_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+ wurde von [a-zA-Z0-9_]+ aus der Fraktion geschmissen.$");
    private static final Pattern INVITE_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+ ist der Fraktion mit Rang \\d beigetreten.$");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        val message = e.getMessage();
        val unformattedText = message.getUnformattedText();

        if (unformattedText.startsWith(" \u00bb Freundesliste:") && unformattedText.endsWith("ist nun online.")) {
            String friendName = unformattedText.split(" ")[3];

            modifyFriendJoin(message, friendName);
        }

        if (ConfigUtil.blockResourcePackReminder && RESOURCEPACK_PATTERN.matcher(unformattedText).find()) {
            e.setCanceled(true);
            return;
        }

        EntityPlayerSP p = Main.MINECRAFT.player;

        if (ConfigUtil.inviteAnnouncement) {
            if (INVITE_PATTERN.matcher(unformattedText).find()) {
                p.playSound(SoundUtil.PLAYER_INVITED, 1, 1);
            } else if (UNINVITE_PATTERN.matcher(unformattedText).find()) {
                p.playSound(SoundUtil.PLAYER_UNINVITED, 1, 1);
            }
        }

        if (ConfigUtil.reportAnnouncement
                && (unformattedText.startsWith("Es liegt ein neuer Report")
                || unformattedText.startsWith("\u00a7cEs liegt ein neuer Report"))) {
            p.playSound(SoundUtil.REPORT_RECEIVED, 1, 1);
        }

        if (ConfigUtil.bombAnnouncement && unformattedText.startsWith("News: ACHTUNG! Es wurde eine Bombe in der N\u00e4he von")) {
            p.playSound(SoundUtil.BOMB_PLACED, 0.15F, 1);
        }

        if (ConfigUtil.contractFulfilledAnnouncement
                && unformattedText.startsWith("[Contract] ")
                && unformattedText.contains(" get\u00f6tet. Kopfgeld: ")) {
            p.playSound(SoundUtil.CONTRACT_FULFILLED, 1, 1);
        }

        if (ConfigUtil.contractAnnouncement && unformattedText.startsWith("[Contract] Es wurde ein Kopfgeld auf")) {
            p.playSound(SoundUtil.CONTRACT_PLACED, 1, 1);
        }

        if (ConfigUtil.serviceAnnouncement &&
                (unformattedText.startsWith("HQ: Achtung! Ein Notruf von ")
                        || unformattedText.startsWith("Achtung! Ein Notruf von "))) {
            p.playSound(SoundUtil.SERVICE_RECEIVED, 1, 1);
        }
    }

    private static void modifyFriendJoin(ITextComponent message, String friendName) {
        TextComponentString callComponent = new TextComponentString(" [\u260e]");
        callComponent.getStyle().setColor(TextFormatting.DARK_GREEN);

        TextComponentString callHoverText = new TextComponentString("Rufe " + friendName + " an");
        callHoverText.getStyle().setColor(TextFormatting.DARK_GREEN);

        callComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, callHoverText));
        callComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acall " + friendName));

        TextComponentString smsComponent = new TextComponentString("[\u2709]");
        smsComponent.getStyle().setColor(TextFormatting.GREEN);

        TextComponentString smsHoverText = new TextComponentString("Schreibe eine SMS an " + friendName);
        smsHoverText.getStyle().setColor(TextFormatting.GREEN);

        smsComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, smsHoverText));
        smsComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/asms " + friendName + " "));

        TextComponentString deleteComponent = new TextComponentString("[\u2717]");
        deleteComponent.getStyle().setColor(TextFormatting.RED);

        TextComponentString deleteHoverText = new TextComponentString("L\u00f6sche " + friendName + " als Freund");
        deleteHoverText.getStyle().setColor(TextFormatting.RED);

        deleteComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, deleteHoverText));
        deleteComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend delete " + friendName));

        message.appendSibling(callComponent).appendSibling(smsComponent).appendSibling(deleteComponent);
    }
}
