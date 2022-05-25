package de.fuzzlemann.ucutils.checks;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
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

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {

        String msg = e.getMessage().getUnformattedText();
        if (msg.equals("Du hast dein Handy genommen.")) {
            hasCommunications = true;
            return;
        }

        Matcher communicationsTaken = PLAYER_TOOK_COMMUNICATIONS_PATTERN.matcher(msg);
        if (communicationsTaken.find()) hasCommunications = false;
    }

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent e) {
        if (e.getGui() instanceof GuiChest) {
            GuiChest mobileGui = (GuiChest) e.getGui();
            if (mobileGui.inventorySlots instanceof ContainerChest) {
                ContainerChest mobileContainer = (ContainerChest) mobileGui.inventorySlots;
                if (mobileContainer.getLowerChestInventory().hasCustomName()) {
                    if (mobileContainer.getLowerChestInventory().getDisplayName().getUnformattedText().equals("§6Telefon")) {
                        hasCommunications = true;
                        if (activeCommunicationsCheck) {
                            activeCommunicationsCheck = false;
                            mobileContainer.getLowerChestInventory().closeInventory((EntityPlayer) AbstractionLayer.getPlayer());
                        }
                    }
                }
            }
        }
    }
}