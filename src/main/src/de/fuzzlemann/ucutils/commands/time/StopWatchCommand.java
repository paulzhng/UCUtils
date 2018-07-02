package de.fuzzlemann.ucutils.commands.time;

import de.fuzzlemann.ucutils.utils.FormatUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class StopWatchCommand implements CommandExecutor {

    private long start = -1L;

    @Override
    @Command(labels = {"stopwatch", "stoppuhr"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (start == -1) {
            start = System.currentTimeMillis();

            TextComponentString text = new TextComponentString("Du hast eine Stoppuhr gestartet.");
            text.getStyle().setColor(TextFormatting.AQUA);

            p.sendMessage(text);
        } else {
            long difference = System.currentTimeMillis() - start;
            start = -1;

            TextComponentString text = new TextComponentString("Die Zeit zwischen dem Beginn und Stopp: ");
            text.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString time = new TextComponentString(FormatUtils.formatMilliseconds(difference));
            time.getStyle().setColor(TextFormatting.RED);

            p.sendMessage(text.appendSibling(time));
        }

        return true;
    }
}