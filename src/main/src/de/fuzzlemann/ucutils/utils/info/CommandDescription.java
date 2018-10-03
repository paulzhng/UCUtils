package de.fuzzlemann.ucutils.utils.info;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Fuzzlemann
 */
public class CommandDescription {

    private final String command;
    private final String description;

    public CommandDescription(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public ITextComponent constructMessage() {
        TextComponentString commandComponent = new TextComponentString(command);
        commandComponent.getStyle().setColor(TextFormatting.GREEN);

        TextComponentString delimiterComponent = new TextComponentString(": ");
        delimiterComponent.getStyle().setColor(TextFormatting.GRAY);

        TextComponentString descriptionComponent = new TextComponentString(description);
        descriptionComponent.getStyle().setColor(TextFormatting.DARK_AQUA);

        return getPrefix().appendSibling(commandComponent).appendSibling(delimiterComponent).appendSibling(descriptionComponent);
    }

    private ITextComponent getPrefix() {
        TextComponentString prefix = new TextComponentString(" » ");
        prefix.getStyle().setColor(TextFormatting.GRAY);

        return prefix;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("command", command)
                .append("description", description)
                .toString();
    }
}
