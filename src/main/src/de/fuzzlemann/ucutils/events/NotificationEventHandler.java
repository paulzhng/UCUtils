package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.SoundUtil;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import lombok.val;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class NotificationEventHandler {
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        val message = e.getMessage();
        val unformattedText = message.getUnformattedText();

        if (ConfigUtil.blockResourcePackReminder
                && (unformattedText.equals("Wir empfehlen dir unser Resourcepack zu nutzen.")
                || unformattedText.equals("Unter http://server.unicacity.de/dl/UnicaCity.zip kannst du es dir herunterladen."))) {
            e.setCanceled(true);
            return;
        }

        EntityPlayerSP p = Main.MINECRAFT.player;

        if (ConfigUtil.inviteAnnouncement) {
            if (unformattedText.startsWith("Invite: ") && unformattedText.endsWith("beigetreten.")) {
                p.playSound(SoundUtil.PLAYER_INVITED, 1, 1);
            } else if (unformattedText.startsWith("Uninvite: ") && unformattedText.endsWith("aus der Fraktion geschmissen.")) {
                p.playSound(SoundUtil.PLAYER_UNINVITED, 1, 1);
            }
        }

        if (ConfigUtil.reportAnnouncement
                && (unformattedText.startsWith("Es liegt ein neuer Report")
                || unformattedText.startsWith("\u00a7cEs liegt ein neuer Report"))) {
            p.playSound(SoundUtil.REPORT_RECEIVED, 1, 1);
        }

        if (ConfigUtil.bombAnnouncement && unformattedText.startsWith("News: ACHTUNG! Es wurde eine Bombe in der N�he von")) {
            p.playSound(SoundUtil.BOMB_PLACED, 0.15F, 1);
        }

        if (ConfigUtil.contractFulfilledAnnouncement
                && unformattedText.startsWith("[Contract] ")
                && unformattedText.contains(" get�tet. Kopfgeld: ")) {
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
}