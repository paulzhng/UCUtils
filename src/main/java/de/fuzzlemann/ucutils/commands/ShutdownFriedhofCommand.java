package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ShutdownFriedhofCommand {

    private static boolean shutdown;

    @SubscribeEvent
    public static void onChat(ClientChatReceivedEvent e) {
        if (!shutdown) return;
        if (!e.getMessage().getUnformattedText().equals("Du lebst nun wieder.")) return;

        ForgeUtils.shutdownPC();
    }

    @Command({"shutdownf", "shutdownfriedhof"})
    public boolean onCommand() {
        shutdown = !shutdown;

        if (shutdown) {
            TextUtils.simpleMessage("Der Friedhof-Shutdown wurde initiiert: Wenn du wieder lebst, wird dein Computer heruntergefahren.");
        } else {
            TextUtils.simpleMessage("Der Friedhof-Shutdown wurde abgebrochen.");
        }

        return true;
    }
}
