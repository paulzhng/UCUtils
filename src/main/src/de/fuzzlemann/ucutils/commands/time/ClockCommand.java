package de.fuzzlemann.ucutils.commands.time;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ClockCommand implements CommandExecutor {
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss 'Uhr'");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    @Command(labels = {"clock", "uhrzeit", "uhr"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        Date date = new Date();

        TextComponentString text = new TextComponentString("Es ist ");
        text.getStyle().setColor(TextFormatting.AQUA);

        TextComponentString timeComponent = new TextComponentString(timeFormat.format(date));
        timeComponent.getStyle().setColor(TextFormatting.RED);

        TextComponentString midText = new TextComponentString(" und der ");
        midText.getStyle().setColor(TextFormatting.AQUA);

        TextComponentString dateComponent = new TextComponentString(dateFormat.format(date));
        dateComponent.getStyle().setColor(TextFormatting.RED);

        TextComponentString endText = new TextComponentString(".");
        endText.getStyle().setColor(TextFormatting.AQUA);

        p.sendMessage(text.appendSibling(timeComponent).appendSibling(midText).appendSibling(dateComponent).appendSibling(endText));
        return true;
    }
}