package de.fuzzlemann.ucutils.utils;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ForgeUtils {

    public static String getMostMatchingPlayer(String name) {
        return getMostMatching(ForgeUtils.getOnlinePlayers(), name);
    }

    public static <T extends String> T getMostMatching(Iterable<T> list, String input) {
        return getMostMatching(list, input, x -> x);
    }

    public static <T> T getMostMatchingL(Iterable<T> list, String input, Function<T, Iterable<String>> toStringIterableFunction) {
        input = input.toLowerCase();

        Map<String, T> inputs = new HashMap<>();
        for (T t : list) {
            Iterable<String> tStrings = toStringIterableFunction.apply(t);
            for (String tString : tStrings) {
                inputs.put(tString, t);
            }
        }

        String found = getMostMatching(inputs.keySet(), input);
        return inputs.get(found);
    }

    public static <T> T getMostMatching(Iterable<T> list, String input, Function<T, String> toStringFunction) {
        input = input.toLowerCase();

        int delta = Integer.MAX_VALUE;
        T found = null;
        for (T t : list) {
            String string = toStringFunction.apply(t).toLowerCase();
            if (!string.startsWith(input)) continue;

            int curDelta = Math.abs(string.length() - input.length());
            if (curDelta < delta) {
                found = t;
                delta = curDelta;
            }

            if (curDelta == 0) break;
        }

        return found;
    }

    public static List<String> getOnlinePlayers() {
        Minecraft minecraft = Main.MINECRAFT;
        NetHandlerPlayClient connection = minecraft.getConnection();

        if (connection == null) return Collections.emptyList();

        Collection<NetworkPlayerInfo> playerInfoList = connection.getPlayerInfoMap();

        return playerInfoList.stream()
                .map(playerInfo -> playerInfo.getGameProfile().getName())
                .map(TextUtils::stripColor)
                .map(TextUtils::stripPrefix)
                .sorted()
                .collect(Collectors.toList());
    }

    public static String getTablistName(NetworkPlayerInfo networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null
                ? networkPlayerInfoIn.getDisplayName().getFormattedText()
                : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }

    public static void shutdownPC() {
        String shutdownCommand;

        if (SystemUtils.IS_OS_AIX) {
            shutdownCommand = "shutdown -Fh now";
        } else if (SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS) {
            shutdownCommand = "shutdown -y -i5 -gnow";
        } else if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_UNIX) {
            shutdownCommand = "shutdown -h now";
        } else if (SystemUtils.IS_OS_HP_UX) {
            shutdownCommand = "shutdown -hy now";
        } else if (SystemUtils.IS_OS_IRIX) {
            shutdownCommand = "shutdown -y -g now";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            shutdownCommand = "shutdown -s -t 0";
        } else {
            return;
        }

        try {
            Runtime.getRuntime().exec(shutdownCommand);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
