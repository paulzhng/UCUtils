package de.fuzzlemann.ucutils.utils;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ForgeUtils {

    public static List<String> getOnlinePlayers() {
        Minecraft minecraft = Main.MINECRAFT;
        NetHandlerPlayClient connection = minecraft.getConnection();

        if (connection == null) {
            return Collections.emptyList();
        }

        Collection<NetworkPlayerInfo> playerInfoMap = connection.getPlayerInfoMap();
        GuiPlayerTabOverlay tabOverlay = minecraft.ingameGUI.getTabList();

        return playerInfoMap.stream()
                .map(tabOverlay::getPlayerName)
                .map(TextUtils::stripColor)
                .map(TextUtils::stripPrefix)
                .sorted()
                .collect(Collectors.toList());
    }
}
