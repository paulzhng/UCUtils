package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.utils.abstraction.AbstractionLayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class CheckKFZEventHandler {

    private static final Pattern CHECK_KFZ_PATTERN = Pattern.compile("^HQ: Das Fahrzeug mit dem Kennzeichen (?:null|.+) ist auf den Spieler (?:\\[UC])*([a-zA-Z0-9_]+) registriert, over.$" +
            "|^Kennzeichen: (?:null|.+) \\| Type: [a-zA-Z]+ \\| Besitzer: (?:\\[UC])*([a-zA-Z0-9_]+)$");

    @SubscribeEvent
    public static void onCheckKFZ(ClientChatReceivedEvent e) {
        String text = e.getMessage().getUnformattedText();

        Matcher matcher = CHECK_KFZ_PATTERN.matcher(text);
        if (!matcher.find()) return;

        String name = matcher.group(1);
        if (name == null) name = matcher.group(2);

        AbstractionLayer.getPlayer().sendChatMessage("/memberinfo " + name);
    }
}
