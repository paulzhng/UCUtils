package de.fuzzlemann.ucutils.utils.info;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Fuzzlemann
 */
public class CommandInfo {

    private CommandDescription[] commands;

    public CommandInfo(CommandDescription... commands) {
        this.commands = commands;
    }

    public CommandDescription[] getCommands() {
        return commands;
    }

    public ITextComponent constructMessage(String heading) {
        TextComponentString headingBegin = new TextComponentString("\n  === ");
        headingBegin.getStyle().setColor(TextFormatting.DARK_GRAY);

        TextComponentString headingComponent = new TextComponentString(heading);
        headingComponent.getStyle().setColor(TextFormatting.GOLD);

        TextComponentString headingEnd = new TextComponentString(" ===");
        headingEnd.getStyle().setColor(TextFormatting.DARK_GRAY);

        return headingBegin.appendSibling(headingComponent).appendSibling(headingEnd).appendSibling(constructMessage());
    }

    private ITextComponent constructMessage() {
        TextComponentString text = new TextComponentString("");

        for (CommandDescription command : commands) {
            text.appendText("\n").appendSibling(command.constructMessage());
        }

        return text;
    }

    public CommandInfo append(CommandInfo commandInfo) {
        this.commands = ArrayUtils.addAll(getCommands(), commandInfo.getCommands());

        return this;
    }

    public CommandInfo createCopy() {
        return new CommandInfo(commands);
    }
}
