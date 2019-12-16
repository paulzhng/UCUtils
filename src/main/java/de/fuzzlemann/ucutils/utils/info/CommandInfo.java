package de.fuzzlemann.ucutils.utils.info;

import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.common.udf.data.info.UDFCommandDescription;
import net.minecraft.util.text.TextFormatting;

import java.util.Set;

/**
 * @author Fuzzlemann
 */
public class CommandInfo {

    private Set<UDFCommandDescription> commands;

    public CommandInfo(Set<UDFCommandDescription> commands) {
        this.commands = commands;
    }

    public Set<UDFCommandDescription> getCommands() {
        return commands;
    }

    public Message constructMessage(String heading) {
        return Message.builder()
                .newLine()
                .of("  === ").color(TextFormatting.DARK_GRAY).advance()
                .of(heading).color(TextFormatting.GOLD).advance()
                .of(" ===").color(TextFormatting.DARK_GRAY).advance()
                .newLine()
                .joiner(commands)
                .consumer((builder, commandDescription) ->
                        builder.of(commandDescription.getCommand()).color(TextFormatting.GREEN).advance()
                                .of(": ").color(TextFormatting.GRAY).advance()
                                .of(commandDescription.getDescription()).color(TextFormatting.DARK_AQUA).advance())
                .newLineJoiner().advance()
                .build();
    }
}
