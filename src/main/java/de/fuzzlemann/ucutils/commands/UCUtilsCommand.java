package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fuzzlemann
 */
public class UCUtilsCommand {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final long compileTime = 1560328503227L; //updated by gradle
    private final String formattedTime = dateFormat.format(new Date(compileTime));

    @Command("ucutils")
    public boolean onCommand() {
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
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Commands anzeigen", TextFormatting.GRAY))
                .advance()
                .add("  ")
                .of("Source-Code").bold().color(TextFormatting.BLUE)
                .clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Fuzzlemann/UCUtils")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Source-Code anzeigen", TextFormatting.GRAY))
                .advance()
                .add("  ")
                .of("Sonstige Features").bold().color(TextFormatting.GRAY)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Sonstige Features anzeigen", TextFormatting.GRAY))
                .clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Fuzzlemann/UCUtils/wiki/Sonstige-Features").advance()
                .newLine()
                .prefix()
                .of("~ by ").color(TextFormatting.GRAY).advance()
                .of("Fuzzlemann").color(TextFormatting.BLUE).advance()
                .send();
        return true;
    }
}
