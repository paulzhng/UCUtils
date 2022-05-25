package de.fuzzlemann.ucutils.checks;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RettichLP
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CommunicationsChecker {
    private static boolean connected;
    private static boolean activeCommunicationsCheck;

    private static final Pattern PLAYER_TOOK_COMMUNICATIONS_PATTERN = Pattern.compile("^((?:\\[UC])*[a-zA-Z0-9_]+) hat dir deine Kommunikationsgeräte abgenommen\\.$");

    public static boolean hasCommunications = false;
    public static String noCommunicationsMessage = "Du hast keine Kommunikationsgeräte.";

    @SubscribeEvent
    public static void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        connected = true;
    }

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent e) {
        if (!connected) return;
        connected = false;

        if (!e.getWorld().isRemote) return;

        activeCommunicationsCheck = true;
        AbstractionLayer.getPlayer().sendChatMessage("/mobile");
    }

    /**
     * If the user has set a password for their account, <code>/mobile</code> cannot be listed until the account is unlocked.
     * As a result, <code>hasCommunications</code> remains false. To avoid this, the check is carried out again when the message
     * came that the account was unlocked.
     */
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        String msg = e.getMessage().getUnformattedText();

        if (msg.equals("Du hast deinen Account freigeschaltet.")) {
            activeCommunicationsCheck = true;
            AbstractionLayer.getPlayer().sendChatMessage("/mobile");
            return;
        }

        if (msg.equals("Du hast dein Handy genommen.")) {
            hasCommunications = true;
            return;
        }

        Matcher communicationsTaken = PLAYER_TOOK_COMMUNICATIONS_PATTERN.matcher(msg);
        if (communicationsTaken.find()) hasCommunications = false;
    }

    /**
     * The GuiOpenEvent is called before a Gui is opened. If we don't want the gui to open, we can cancel the event.<br>
     * Problem: Since the content of the gui is only set after opening and we haven't opened the gui, the gui items are
     * placed in the inventory on the client side.<br>
     * Solution: We close the inventory after opening it (and setting the items).
     */
    @SubscribeEvent
    public static void onGuiOpen(GuiContainerEvent.DrawForeground e) {
        if (!(e.getGuiContainer().inventorySlots instanceof ContainerChest)) return;
        ContainerChest containerChest = (ContainerChest) e.getGuiContainer().inventorySlots;

        if (!containerChest.getLowerChestInventory().getDisplayName().getUnformattedText().equals("§6Telefon")) return;

        hasCommunications = true;

        if (activeCommunicationsCheck) {
            activeCommunicationsCheck = false;
            Minecraft.getMinecraft().player.closeScreen();
        }
    }
}