package de.fuzzlemann.ucutils.update;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
@DataModule("Update-Reminder")
public class UpdateReminder implements DataLoader {
    private static boolean updateNeeded;
    private static boolean connected;

    @SubscribeEvent
    public static void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        connected = true;
    }

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent e) {
        if (!connected) return;
        connected = false;

        if (!updateNeeded) return;

        Message.builder()
                .of("Es ist ein neues Update von UCUtils verfügbar!").color(TextFormatting.RED).advance()
                .newLine()
                .of("Du kannst das Update ").color(TextFormatting.RED).advance()
                .of("hier").color(TextFormatting.RED)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Download", TextFormatting.GREEN))
                .clickEvent(ClickEvent.Action.OPEN_URL, "https://fuzzlemann.de/UCUtils.jar").advance()
                .of(" herunterladen oder mittels ").color(TextFormatting.RED).advance()
                .of("/updateucutils").color(TextFormatting.RED)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Ausführen", TextFormatting.GREEN))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/updateucutils").advance()
                .of("direkt updaten.").color(TextFormatting.RED).advance()
                .send();
    }

    @Override
    public void load() {
        updateNeeded = getCurrentVersion() < getLatestVersion();
    }

    private int getCurrentVersion() {
        return parseVersion(Main.VERSION);
    }

    private int getLatestVersion() {
        return parseVersion(APIUtils.get("http://fuzzlemann.de/latestversion.html"));
    }

    private int parseVersion(String versionString) {
        return Integer.parseInt(versionString.split("-")[1].replace(".", ""));
    }
}
