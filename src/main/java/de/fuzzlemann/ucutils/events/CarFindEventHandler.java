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
    private static final Pattern ROUTE_PATTERNS = Pattern.compile("^Du hast keine Route\\.$" +
            "|^Du hast deine Route gelöscht\\.$");


    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (!UCUtilsConfig.autoNavigationForCarFind) return;

        Matcher routeMatcher = ROUTE_PATTERNS.matcher(e.getMessage().getUnformattedText());
        if (routeMatcher.find())
            e.setCanceled(true);
        
        Matcher carPositionMessageMatcher = CAR_POSITION_MESSAGE.matcher(e.getMessage().getUnformattedText());
        if (carPositionMessageMatcher.find()) {
            AbstractionLayer.getPlayer().sendChatMessage("/stoproute");
            AbstractionLayer.getPlayer().sendChatMessage("/navi " + carPositionMessageMatcher.group(1) + "/" + carPositionMessageMatcher.group(2) + "/" + carPositionMessageMatcher.group(3));
        }


    }
}
