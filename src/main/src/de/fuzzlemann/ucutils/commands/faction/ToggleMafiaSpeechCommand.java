package de.fuzzlemann.ucutils.commands.faction;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class ToggleMafiaSpeechCommand implements CommandExecutor {

    private static boolean activated;
    private static final Pattern VOCALS_PATTERN = Pattern.compile("[aeiou\u00f6\u00e4\u00fcAEIOU\u00c4\u00dc\u00d6\u00e0\u00e1\u00e8\u00e9\u00ec\u00ed\u00f3\u00f2\u00f9\u00fa\u00c0\u00c1\u00c8\u00c9\u00cc\u00cd\u00d2\u00d3\u00d9\u00da]");
    private static final Set<String> EXCLUDED = Sets.newHashSet("xd");
    private static final List<Map.Entry<String, String>> REPLACE_IGNORE_CASE = Lists.newArrayList(
            Maps.immutableEntry("guten tag", "buongiorno"),
            Maps.immutableEntry("tsch\u00fcss", "arrivederci"),
            Maps.immutableEntry("junge", "ragazzo"),
            Maps.immutableEntry("m\u00e4dchen", "ragazza"),
            Maps.immutableEntry("Wollen Sie Drogen", "Vogliono la droga"),
            Maps.immutableEntry("waffen", "stemma"),
            Maps.immutableEntry("Wie geht es dir", "Come stai"),
            Maps.immutableEntry("Guten Abend", "Buona sera"),
            Maps.immutableEntry("Sch\u00f6nen Abend", "Buona serata")
    );
    private static final List<Map.Entry<String, String>> REPLACE_RETAIN_CASE = Lists.newArrayList(
            Maps.immutableEntry("ja", "s\u00ec"),
            Maps.immutableEntry("danke", "grazie"),
            Maps.immutableEntry("bitte", "prego")
    );
    private static final Set<String> COMMANDS = Sets.newHashSet("asms", "sms", "w", "close", "whisper", "s", "schreien");

    @Override
    @Command(labels = {"togglemafiaspeech", "togmafiaspeech", "togglemafia", "togmafia"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        activated = !activated;

        ITextComponent text = activated
                ? TextUtils.simpleMessage("Du hast die Mafiasprache eingeschalten.", TextFormatting.GREEN)
                : TextUtils.simpleMessage("Du hast die Mafiasprache ausgeschalten.", TextFormatting.RED);

        p.sendMessage(text);
        return true;
    }

    @SubscribeEvent
    public static void onChat(ClientChatEvent e) {
        if (!activated) return;

        String message = e.getMessage();
        String[] splitted = message.split(" ");

        if (message.startsWith("/")) {
            if (splitted.length == 1) return;

            String command = splitted[0].toLowerCase();
            command = command.substring(1, command.length());

            if (COMMANDS.contains(command)) {
                int ignoredPositions = command.equals("sms") || command.equals("asms") ? 2 : 1;

                String[] unmodifiable = new String[ignoredPositions];
                System.arraycopy(splitted, 0, unmodifiable, 0, ignoredPositions);

                String[] modifiable = new String[splitted.length - ignoredPositions];
                System.arraycopy(splitted, ignoredPositions, modifiable, 0, splitted.length - ignoredPositions);

                String modifiableString = turnIntoMafiaSpeech(modifiable);

                String fullCommand = String.join(" ", unmodifiable);
                if (!modifiableString.isEmpty())
                    fullCommand += " " + modifiableString;

                e.setMessage(fullCommand);
            }

            return;
        }

        e.setMessage(turnIntoMafiaSpeech(splitted));
    }

    private static String turnIntoMafiaSpeech(String[] words) {
        String fullString = String.join(" ", words);
        for (Map.Entry<String, String> entry : REPLACE_IGNORE_CASE) {
            String toReplace = entry.getKey();
            String replaceTo = entry.getValue();

            fullString = fullString.replaceAll("(?i)" + toReplace, replaceTo);
        }

        words = fullString.split(" ");
        StringJoiner stringJoiner = new StringJoiner(" ");

        for (String word : words) {
            if (word.isEmpty()) continue;
            if (EXCLUDED.contains(word.toLowerCase())) {
                stringJoiner.add(word);
                continue;
            }

            String lastChar = word.substring(word.length() - 1);

            int lastIndex = word.length() - 1;
            int i = lastIndex;
            while (lastIndex != 0 && i != 1 && !CharMatcher.javaLetter().matches(lastChar.toCharArray()[0])) {
                lastChar = word.substring(--i, i + 1);
            }

            if (CharMatcher.javaLetter().matches(lastChar.toCharArray()[0])) {
                String replaceWord = word;

                boolean addSpecialCharacters = lastIndex != i;
                if (addSpecialCharacters) {
                    replaceWord = replaceRetainingCase(replaceWord.substring(0, i + 1));
                } else {
                    replaceWord = replaceRetainingCase(replaceWord);
                }

                boolean addE = !VOCALS_PATTERN.matcher(String.valueOf(replaceWord.charAt(replaceWord.length() - 1))).find();

                if (addE)
                    replaceWord += "e";

                if (addSpecialCharacters)
                    replaceWord += word.substring(i + 1, lastIndex + 1);

                word = replaceWord;
            }

            stringJoiner.add(word);
        }

        return stringJoiner.toString();
    }

    private static String replaceRetainingCase(String toReplaceString) {
        for (Map.Entry<String, String> entry : REPLACE_RETAIN_CASE) {
            String toReplace = entry.getKey();
            String replaceTo = entry.getValue();

            if (toReplace.equalsIgnoreCase(toReplaceString))
                toReplaceString = replaceRetainingCase(toReplaceString, replaceTo);
        }

        return toReplaceString;
    }

    private static String replaceRetainingCase(String toReplace, String replaceTo) {
        return toReplace.replaceAll("(?i)" + toReplace, replaceTo.chars()
                .mapToObj(i -> (char) i)
                .map(i -> {
                    int index = replaceTo.indexOf(i);
                    boolean lowerCase = index > toReplace.length() - 1;

                    if (!lowerCase)
                        lowerCase = Character.isLowerCase(toReplace.charAt(index));

                    return lowerCase
                            ? Character.toString(Character.toLowerCase(i))
                            : Character.toString(Character.toUpperCase(i));
                })
                .collect(Collectors.joining()));
    }
}
