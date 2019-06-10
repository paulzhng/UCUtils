package de.fuzzlemann.ucutils.utils.faction.police;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

            String command = splitted[0];
            if (!command.equalsIgnoreCase("/frisk")) return;

            String nameString = splitted[1];
            String foundName = ForgeUtils.getMostMatchingPlayer(nameString);

            if (foundName == null) return;
            lastName = foundName;
        }
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent textComponent = e.getMessage();
        String message = textComponent.getUnformattedText();

        Matcher showInvMatcher = SHOW_INV_PATTERN.matcher(message);
        if (showInvMatcher.find()) lastName = showInvMatcher.group(1);
        if (lastName == null) return;

        Matcher marijuanaMatcher = MARIJUANA_PATTERN.matcher(message);
        if (!marijuanaMatcher.find()) return;

        String group1 = marijuanaMatcher.group(1);
        String amountString = group1 == null ? marijuanaMatcher.group(2) : group1;

        int amount = Integer.parseInt(amountString);

        boolean allowed = amount == 0 || amount <= 12 && hasMedicalLicense(lastName);

        Message msg;
        if (allowed) {
            msg = Message.builder()
                    .space()
                    .of("(Erlaubt)")
                    .color(TextFormatting.GREEN)
                    .advance().build();
        } else {
            msg = Message.builder().space().of("(Nicht erlaubt)").color(TextFormatting.RED).advance().build();
        }

        textComponent.appendSibling(msg.toTextComponent());
        lastName = null;
    }

    public static boolean hasMedicalLicense(String playerName) {
        try {
            String response = APIUtils.post("http://tomcat.fuzzlemann.de/factiononline/checkmedicallicense", "name", playerName);
            return Boolean.valueOf(response);
        } catch (Exception e) {
            Logger.LOGGER.catching(e);
            return false;
        }
    }
}
