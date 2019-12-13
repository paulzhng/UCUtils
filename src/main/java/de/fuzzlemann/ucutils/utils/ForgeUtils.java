package de.fuzzlemann.ucutils.utils;

import com.google.common.collect.Maps;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.SystemUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ForgeUtils {

    private static boolean isTest;
    private static boolean labyMod;

    static {
        try {
            Class.forName("net.labymod.main.LabyMod");
            labyMod = true;
        } catch (ClassNotFoundException e) {
            labyMod = false;
        }

        try {
            Class.forName("de.fuzzlemann.ucutils.base.command.execution.CommandTest");
            isTest = true;
        } catch (ClassNotFoundException e) {
            isTest = false;
        }
    }

    public static boolean isTest() {
        return isTest;
    }

    public static boolean hasLabyMod() {
        return labyMod;
    }

    public static void makeScreenshot(File target) {
        Framebuffer framebuffer = ReflectionUtil.getValue(Main.MINECRAFT, Framebuffer.class);
        assert framebuffer != null;

        BufferedImage image = ScreenShotHelper.createScreenshot(Main.MINECRAFT.displayWidth, Main.MINECRAFT.displayHeight, framebuffer);

        try {
            target.createNewFile();
            ImageIO.write(image, "jpg", target);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> Map.Entry<Double, T> getNearestObject(T[] array, Function<T, BlockPos> blockPosFunction) {
        return getNearestObject(Arrays.asList(array), blockPosFunction);
    }

    public static <T> Map.Entry<Double, T> getNearestObject(Iterable<T> iterable, Function<T, BlockPos> blockPosFunction) {
        return getNearestObject(AbstractionLayer.getPlayer().getPosition(), iterable, blockPosFunction);
    }

    public static <T> Map.Entry<Double, T> getNearestObject(T[] array, Function<T, Integer> xFunction, Function<T, Integer> yFunction, Function<T, Integer> zFunction) {
        return getNearestObject(Arrays.asList(array), xFunction, yFunction, zFunction);
    }

    public static <T> Map.Entry<Double, T> getNearestObject(Iterable<T> iterable, Function<T, Integer> xFunction, Function<T, Integer> yFunction, Function<T, Integer> zFunction) {
        return getNearestObject(AbstractionLayer.getPlayer().getPosition(), iterable, xFunction, yFunction, zFunction);
    }

    public static <T> Map.Entry<Double, T> getNearestObject(BlockPos blockPos, T[] array, Function<T, BlockPos> blockPosFunction) {
        return getNearestObject(blockPos, Arrays.asList(array), blockPosFunction);
    }

    public static <T> Map.Entry<Double, T> getNearestObject(BlockPos blockPos, Iterable<T> iterable, Function<T, BlockPos> blockPosFunction) {
        Function<T, Integer> xFunction = blockPosFunction.andThen(Vec3i::getX);
        Function<T, Integer> yFunction = blockPosFunction.andThen(Vec3i::getY);
        Function<T, Integer> zFunction = blockPosFunction.andThen(Vec3i::getZ);

        return getNearestObject(blockPos, iterable, xFunction, yFunction, zFunction);
    }

    public static <T> Map.Entry<Double, T> getNearestObject(BlockPos pos, T[] array, Function<T, Integer> xFunction, Function<T, Integer> yFunction, Function<T, Integer> zFunction) {
        return getNearestObject(pos, Arrays.asList(array), xFunction, yFunction, zFunction);
    }

    public static <T> Map.Entry<Double, T> getNearestObject(BlockPos pos, Iterable<T> iterable, Function<T, Integer> xFunction, Function<T, Integer> yFunction, Function<T, Integer> zFunction) {
        T foundT = null;
        double nearestDistance = Double.MAX_VALUE;
        for (T t : iterable) {
            int x = xFunction.apply(t);
            int y = yFunction.apply(t);
            int z = zFunction.apply(t);

            double distance = pos.getDistance(x, y, z);

            if (distance < nearestDistance) {
                nearestDistance = distance;
                foundT = t;
            }
        }

        return Maps.immutableEntry(nearestDistance, foundT);
    }

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
        } catch (IOException e) {
            Logger.LOGGER.catching(e);
        }
    }
}
