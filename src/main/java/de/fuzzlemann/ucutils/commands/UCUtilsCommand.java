package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.teamspeak.exceptions.ClientQueryException;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.command.api.TabCompletion;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class UCUtilsCommand implements TabCompletion {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final long compileTime = 1570017583699L; //updated by gradle
    private final String formattedTime = dateFormat.format(new Date(compileTime));

    @Command("ucutils")
    public boolean onCommand(@CommandParam(required = false, requiredValue = "teamSpeakReconnect") boolean teamSpeakReconnect) {
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

        Message.builder()
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
                .of("Fuzzlemann").color(TextFormatting.BLUE).advance()
                .send();
        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.singletonList("teamSpeakReconnect");

        return null;
    }
}
