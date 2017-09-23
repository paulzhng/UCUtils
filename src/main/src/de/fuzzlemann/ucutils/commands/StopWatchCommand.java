package de.fuzzlemann.ucutils.commands;

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

            TextComponentString time = new TextComponentString(formatMilliseconds(difference));
            time.getStyle().setColor(TextFormatting.RED);

            p.sendMessage(text.appendSibling(time));
        }

        return true;
    }

    private static String formatMilliseconds(long ms) {
        StringBuilder sb = new StringBuilder();
        long milliseconds = ms % 1000;
        ms /= 1000;
        long seconds = ms % 60;
        ms /= 60;
        long minutes = ms % 60;
        ms /= 60;
        long hours = ms % 24;

        if (hours != 0) {
            sb.append(hours).append(hours == 1 ? " Stunde " : " Stunden ");
        }

        if (minutes != 0) {
            sb.append(minutes).append(minutes == 1 ? " Minute " : " Minuten ");
        }

        if (seconds != 0) {
            sb.append(seconds).append(seconds == 1 ? " Sekunde " : " Sekunden ");
        }

        if (milliseconds != 0) {
            sb.append(milliseconds).append(milliseconds == 1 ? " Millisekunde" : " Millisekunden");
        }

        return sb.toString();
    }
}