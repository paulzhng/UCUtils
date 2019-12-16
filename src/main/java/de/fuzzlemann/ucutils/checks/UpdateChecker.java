package de.fuzzlemann.ucutils.checks;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
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
@UDFModule(value = DataRegistry.VERSION, version = 1)
public class UpdateChecker implements UDFLoader<String> {
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
    public void supply(String latestVersion) {
        updateNeeded = getCurrentVersion() < parseVersion(latestVersion);
    }

    private int getCurrentVersion() {
        return parseVersion(Main.VERSION);
    }

    private int parseVersion(String versionString) {
        return Integer.parseInt(versionString.split("-")[1].replace(".", ""));
    }
}
