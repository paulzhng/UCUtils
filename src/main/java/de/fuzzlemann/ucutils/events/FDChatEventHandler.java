package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.checks.CommunicationsChecker;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author RettichLP
 */
@Mod.EventBusSubscriber
public class FDChatEventHandler {

    @SubscribeEvent
    public static void onFDChatSend(ClientChatEvent e) {
        String msg = e.getMessage();

        if (msg.startsWith("/f ") && !CommunicationsChecker.hasCommunications) {
            TextUtils.error(CommunicationsChecker.noCommunicationsMessage + " Wenn du die Nachricht trotzdem senden möchtest, nutze /fforce [Nachricht]");
            e.setMessage(null);
            return;
        }

        if (msg.startsWith("/sf ") && !CommunicationsChecker.hasCommunications) {
            TextUtils.error(CommunicationsChecker.noCommunicationsMessage + " Wenn du die Nachricht trotzdem senden möchtest, nutze /sfforce [Nachricht]");
            e.setMessage(null);
            return;
        }

        if (msg.startsWith("/d ") && !CommunicationsChecker.hasCommunications) {
            TextUtils.error(CommunicationsChecker.noCommunicationsMessage + " Wenn du die Nachricht trotzdem senden möchtest, nutze /dforce [Nachricht]");
            e.setMessage(null);
        }
    }
}