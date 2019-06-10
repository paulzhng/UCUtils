package de.fuzzlemann.ucutils.utils.text;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class TextUtils {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Pattern STRIP_PREFIX_PATTERN = Pattern.compile("\\[[0-9A-Za-z]+]");

    public static void error(String message) {
        simpleMessage(message, TextFormatting.RED);
    }

    public static void simpleMessage(String message) {
        simpleMessage(message, TextFormatting.GRAY);
    }

    public static void simpleMessage(String message, TextFormatting color) {
        Message.builder().prefix().of(message).color(color).advance().send();
    }

    public static String stripColor(String string) {
        return STRIP_COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public static String stripPrefix(String string) {
        return STRIP_PREFIX_PATTERN.matcher(string).replaceAll("");
    }
}
