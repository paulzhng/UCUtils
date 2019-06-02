package de.fuzzlemann.ucutils.utils.text;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class TextUtils {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Pattern STRIP_PREFIX_PATTERN = Pattern.compile("\\[[0-9A-Za-z]+]");

    public static void error(String message) {
        simplePrefixMessage(message, TextFormatting.RED);
    }

    public static void simplePrefixMessage(String message) {
        simplePrefixMessage(message, TextFormatting.GRAY);
    }

    public static void simplePrefixMessage(String message, TextFormatting color) {
        Message.builder().prefix().of(message).color(color).advance().send();
    }

    public static void simpleMessage(String message, TextFormatting color) {
        Message.builder().of(message).color(color).advance().send();
    }

    public static String stripColor(String string) {
        Objects.requireNonNull(string);

        return STRIP_COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public static String stripPrefix(String string) {
        Objects.requireNonNull(string);

        return STRIP_PREFIX_PATTERN.matcher(string).replaceAll("");
    }
}
