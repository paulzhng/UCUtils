package de.fuzzlemann.ucutils.commands;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.common.DonationEntry;
import de.fuzzlemann.ucutils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.teamspeak.exceptions.ClientQueryException;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class UCUtilsCommand implements TabCompletion {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final long compileTime = 1598616399515L; // updated by gradle
    private final String formattedTime = dateFormat.format(new Date(compileTime));
    private final Gson gson = new Gson();

    @Command("ucutils")
    public boolean onCommand(@CommandParam(required = false, requiredValue = "teamSpeakReconnect") boolean teamSpeakReconnect,
                             @CommandParam(required = false, requiredValue = "resetAccount") boolean resetAccount) {
        if (teamSpeakReconnect) {
            try {
                TSClientQuery.reconnect();
                TextUtils.simpleMessage("Die TeamSpeak ClientQuery Verbindung wurde neugestartet.");
            } catch (ClientQueryException e) {
                Logger.LOGGER.catching(e);
                TextUtils.error("Ein Fehler ist beim Verbinden zur TeamSpeak ClientQuery aufgetreten.");
            }
            return true;
        }

        if (resetAccount) {
            APIUtils.postAuthenticated("http://tomcat.fuzzlemann.de/factiononline/resetAccount");
            TextUtils.simpleMessage("Dein Account wurde erfolgreich resettet.");
            return true;
        }

        Message.Builder builder = Message.builder()
                .prefix()
                .of("UCUtils ").color(TextFormatting.GRAY).advance()
                .of(Main.VERSION).color(TextFormatting.BLUE).advance()
                .of(" (build: ").color(TextFormatting.GRAY).advance()
                .of(formattedTime + " Uhr").color(TextFormatting.BLUE).advance()
                .of(")").color(TextFormatting.GRAY).advance()
                .newLine()
                .prefix()
                .of("Commands").bold().color(TextFormatting.GRAY)
                .clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Fuzzlemann/UCUtils/wiki/Commands")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Commands anzeigen", TextFormatting.GRAY))
                .advance()
                .add("  ")
                .of("Source-Code").bold().color(TextFormatting.BLUE)
                .clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Fuzzlemann/UCUtils")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Source-Code anzeigen", TextFormatting.GRAY))
                .advance()
                .add("  ")
                .of("Sonstige Features").bold().color(TextFormatting.GRAY)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Sonstige Features anzeigen", TextFormatting.GRAY))
                .clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Fuzzlemann/UCUtils/wiki/Sonstige-Features").advance()
                .newLine()
                .prefix()
                .of("~ by ").color(TextFormatting.GRAY).advance()
                .of("Fuzzlemann").color(TextFormatting.BLUE).advance();

        String response = APIUtils.get("http://tomcat.fuzzlemann.de/factiononline/topDonors");
        if (response != null) {
            List<DonationEntry> topDonations = gson.fromJson(response, new TypeToken<List<DonationEntry>>() {
            }.getType());

            builder.newLine()
                    .prefix()
                    .of("~ Danke an ").color(TextFormatting.GRAY).advance()
                    .joiner(topDonations)
                    .commaJoiner()
                    .andNiceJoiner()
                    .consumer((b, donation) -> b.of(donation.getName()).color(TextFormatting.BLUE)
                            .hoverEvent(HoverEvent.Action.SHOW_TEXT, Message.builder().of("\"").color(TextFormatting.GRAY).advance()
                                    .of(donation.getMessage()).color(TextFormatting.BLUE).advance()
                                    .of("\"").color(TextFormatting.GRAY).advance().build()).advance()
                            .of(" (").color(TextFormatting.GRAY).advance()
                            .of(donation.getAmount() + "€").color(TextFormatting.BLUE).advance()
                            .of(")").color(TextFormatting.GRAY).advance()).advance()
                    .of("!").color(TextFormatting.GRAY).advance();
        }

        builder.send();
        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Arrays.asList("teamSpeakReconnect", "resetAccount");

        return null;
    }
}
