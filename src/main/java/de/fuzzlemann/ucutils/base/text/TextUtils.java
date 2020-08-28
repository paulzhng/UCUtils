package de.fuzzlemann.ucutils.base.text;

import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann, SpigotMC
 * <p>
 * {@link #fromLegacyText(String, TextFormatting, char)} is derived from https://github.com/SpigotMC/BungeeCord/blob/master/chat/src/main/java/net/md_5/bungee/api/chat/TextComponent.java#L21
 */
@SideOnly(Side.CLIENT)
public class TextUtils {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Pattern STRIP_PREFIX_PATTERN = Pattern.compile("\\[[0-9A-Za-z]+]");
    private static final Pattern URL_MATCHER = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$");

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

    /**
     * derived from https://github.com/SpigotMC/BungeeCord/blob/master/chat/src/main/java/net/md_5/bungee/api/chat/TextComponent.java#L21
     * <p>
     *
     * @see #fromLegacyText(String, TextFormatting, char)
     */
    public static Message fromLegacyText(String message) {
        return fromLegacyText(message, TextFormatting.WHITE, '§');
    }

    /**
     * derived from https://github.com/SpigotMC/BungeeCord/blob/master/chat/src/main/java/net/md_5/bungee/api/chat/TextComponent.java#L21
     * <p>
     *
     * @see #fromLegacyText(String)
     */
    public static Message fromLegacyText(String message, TextFormatting defaultColor, char colorChar) {
        Matcher urlMatcher = URL_MATCHER.matcher(message);

        StringBuilder builder = new StringBuilder();
        MessagePart.Builder messagePart = MessagePart.builder();
        Message.Builder messageBuilder = Message.builder();

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == colorChar) {
                if (++i >= message.length()) break;

                c = message.charAt(i);
                if (c >= 'A' && c <= 'Z') c += 32;

                TextFormatting format = TextUtils.byColorCode(c);
                if (format == null) continue;

                if (builder.length() > 0) {
                    MessagePart.Builder old = messagePart;
                    old.message(builder.toString());
                    messageBuilder.messageParts(old.build());

                    messagePart = MessagePart.builder();
                    builder = new StringBuilder();
                }

                switch (format) {
                    case BOLD:
                        messagePart.bold(true);
                        break;
                    case ITALIC:
                        messagePart.italic(true);
                        break;
                    case UNDERLINE:
                        messagePart.underlined(true);
                        break;
                    case STRIKETHROUGH:
                        messagePart.strikethrough(true);
                        break;
                    case OBFUSCATED:
                        messagePart.obfuscated(true);
                        break;
                    case RESET:
                        format = defaultColor;
                    default:
                        messagePart = MessagePart.builder().color(format);
                        break;
                }

                continue;
            }

            int pos = message.indexOf(' ', i);
            if (pos == -1) pos = message.length();

            if (urlMatcher.region(i, pos).find()) { // Web link handling
                if (builder.length() > 0) {
                    MessagePart.Builder old = messagePart;
                    old.message(builder.toString());
                    messageBuilder.messageParts(old.build());

                    messagePart = MessagePart.builder();
                    builder = new StringBuilder();
                }

                MessagePart.Builder old = messagePart;
                messagePart = MessagePart.builder();
                String urlString = message.substring(i, pos);
                messagePart.message(urlString);
                messagePart.clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlString.startsWith("http") ? urlString : "http://" + urlString));
                messageBuilder.messageParts(old.build());
                i += pos - i - 1;
                messagePart = old;
                continue;
            }

            builder.append(c);
        }

        messagePart.message(builder.toString());
        messageBuilder.messageParts(messagePart.build());

        return messageBuilder.build();
    }

    public static TextFormatting byColorCode(char colorCode) {
        for (TextFormatting textFormatting : TextFormatting.values()) {
            try {
                char foundColorCode = ReflectionUtil.getValue(textFormatting, char.class);

                if (foundColorCode == colorCode) return textFormatting;
            } catch (NullPointerException ignore) {
            }
        }

        return null;
    }
}
