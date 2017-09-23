package de.fuzzlemann.ucutils.utils;

import de.fuzzlemann.ucutils.Main;
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
                .map(playerName -> {
                    if (playerName.startsWith("\u00a78[\u00a7r\u00a79UC\u00a7r\u00a78]")) {
                        playerName = playerName.substring(16, playerName.length());
                    }

                    if (playerName.startsWith("\u00a78[\u00a7r\u00a76R\u00a7r\u00a78]")) {
                        playerName = playerName.substring(15, playerName.length());
                    }

                    while (playerName.startsWith("\u00a7")) {
                        playerName = playerName.substring(2, playerName.length());
                    }

                    return playerName;
                })
                .sorted()
                .collect(Collectors.toList());
    }
}
