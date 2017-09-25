package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class InfoCommand implements CommandExecutor {

    @Override
    @Command(labels = "info")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        ITextComponent text = constructText("Fraktionen", "/fcinfo")
                .appendSibling(constructText("Wichtige Befehle", "/cinfo"))
                .appendSibling(constructText("Fraktionsbefehle", "/finfo"));

        p.sendMessage(text);
        return true;
    }

    private ITextComponent constructText(String text, String command) {
        TextComponentString prefix = new TextComponentString("\n \u00bb ");
        prefix.getStyle().setColor(TextFormatting.RED);

        TextComponentString textComponent = new TextComponentString(text);
        textComponent.getStyle().setColor(TextFormatting.DARK_GREEN);

        TextComponentString hoverText = new TextComponentString("Klick mich!");
        hoverText.getStyle().setColor(TextFormatting.GREEN);

        textComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        return prefix.appendSibling(textComponent);
    }
}
