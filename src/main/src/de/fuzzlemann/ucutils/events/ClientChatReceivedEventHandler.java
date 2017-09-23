package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.SoundUtil;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import lombok.val;
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
public class ClientChatReceivedEventHandler {
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        val message = e.getMessage();
        val unformattedText = message.getUnformattedText();

        if (ConfigUtil.blockResourcepackReminder
                && (unformattedText.equals("Wir empfehlen dir unser Resourcepack zu nutzen.")
                || unformattedText.equals("Unter http://server.unicacity.de/dl/UnicaCity.zip kannst du es dir herunterladen."))) {
            e.setCanceled(true);
        }

        if (ConfigUtil.reportAnnouncement && unformattedText.startsWith("Es liegt ein neuer Report von ")) {
            Main.MINECRAFT.player.playSound(SoundUtil.REPORT_RECEIVED, 1, 1);
        }

        if (ConfigUtil.contractAnnouncement && unformattedText.startsWith("[Contract] Es wurde ein Kopfgeld auf")) {
            Main.MINECRAFT.player.playSound(SoundUtil.CONTRACT_PLACED, 1, 1);
        }

        if (ConfigUtil.serviceAnnouncement && unformattedText.startsWith("HQ: Achtung! Ein Notruf von ")) {
            Main.MINECRAFT.player.playSound(SoundUtil.SERVICE_RECEIVED, 1, 1);
        }
    }
}
