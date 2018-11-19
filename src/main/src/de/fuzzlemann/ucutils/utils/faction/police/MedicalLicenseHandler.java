package de.fuzzlemann.ucutils.utils.faction.police;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class MedicalLicenseHandler {

    private static final Pattern SHOW_INV_PATTERN = Pattern.compile("^Tascheninhalt von ([a-zA-Z0-9_]+):$");
    private static final Pattern MARIJUANA_PATTERN = Pattern.compile("^ {2}- Marihuana: (\\d+)$|^ - Gras: (\\d+)g$");
    private static String lastName;

    @SubscribeEvent
    public static void onCommand(ClientChatEvent e) {
        String message = e.getMessage();
        String[] splitted = message.split(" ");

        if (message.startsWith("/")) {
            if (splitted.length < 2) return;

            String command = splitted[0].toLowerCase();
            if (!command.equalsIgnoreCase("/frisk")) return;

            String lowerNameString = splitted[1].toLowerCase();
            String foundName = null;

            int delta = Integer.MAX_VALUE;

            for (String playerName : ForgeUtils.getOnlinePlayers()) {
                String nameLowerCase = playerName.toLowerCase();
                if (!nameLowerCase.startsWith(lowerNameString)) continue;

                int curDelta = Math.abs(nameLowerCase.length() - lowerNameString.length());
                if (curDelta < delta) {
                    foundName = playerName;
                    delta = curDelta;
                }

                if (curDelta == 0) break;
            }

            if (foundName == null) return;
            lastName = foundName;
        }
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent textComponent = e.getMessage();
        String message = textComponent.getUnformattedText();

        Matcher showInvMatcher = SHOW_INV_PATTERN.matcher(message);
        if (showInvMatcher.find()) {
            lastName = showInvMatcher.group(1);
        }

        if (lastName == null) return;

        Matcher marijuanaMatcher = MARIJUANA_PATTERN.matcher(message);
        if (!marijuanaMatcher.find()) return;

        String group1 = marijuanaMatcher.group(1);
        String amountString = group1 == null ? marijuanaMatcher.group(2) : group1;

        int amount = Integer.parseInt(amountString);

        boolean allowed = amount <= 12 && hasMedicalLicense(lastName);

        Message msg;
        if (allowed) {
            msg = Message.builder()
                    .space()
                    .of("(Erlaubt)")
                    .color(TextFormatting.GREEN)
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Die Person besitzt eine medizinische Marihuanalizenz", TextFormatting.GREEN))
                    .advance().build();
        } else {
            msg = Message.builder().space().of("(Nicht erlaubt)").color(TextFormatting.RED).advance().build();
        }

        textComponent.appendSibling(msg.toTextComponent());
        lastName = null;
    }

    public static boolean hasMedicalLicense(String playerName) {
        try {
            URL url = new URL("http://fuzzlemann.de/medicallicense.php?username=" + playerName);
            return Boolean.valueOf(IOUtils.toString(url, StandardCharsets.UTF_8));
        } catch (IOException e) {
            return false;
        }
    }
}
