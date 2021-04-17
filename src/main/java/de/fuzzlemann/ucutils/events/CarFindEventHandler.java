package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dimiikou
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class CarFindEventHandler {
    private static final Pattern CAR_POSITION_MESSAGE = Pattern.compile("^\\[Car] Das Fahrzeug befindet sich bei . X: (-?\\d+) \\| Y: (-?\\d+) \\| Z: (-?\\d+)$");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (!UCUtilsConfig.autoNavigationForCarFind)
            return;
        Matcher m = CAR_POSITION_MESSAGE.matcher(e.getMessage().getUnformattedText());
        if (m.find())
            AbstractionLayer.getPlayer().sendChatMessage("/navi " + m.group(1) + "/" + m.group(2) + "/" + m.group(3));
    }
}
