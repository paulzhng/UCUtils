package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class InfoCommand {

    @Command("info")
    public boolean onCommand() {
        Message.builder()
                .messageParts(constructText("Fraktionen", "/finfo").getMessageParts())
                .messageParts(constructText("Wichtige Befehle", "/cinfo").getMessageParts())
                .messageParts(constructText("Fraktionsbefehle", "/fcinfo").getMessageParts())
                .send();
        return true;
    }

    private Message constructText(String text, String command) {
        return Message.builder()
                .newLine()
                .of(" » ").color(TextFormatting.RED).advance()
                .of(text).color(TextFormatting.DARK_GREEN)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Informationen abrufen", TextFormatting.GREEN))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, command).advance()
                .build();
    }
}
