package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class TimerCommand implements CommandExecutor {
    private final Timer TIMER = new Timer();
    private int timerID = 0;

    @Override
    @Command(labels = "timer", usage = "/%label% [Zeit in Sekunden]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        int seconds;
        try {
            seconds = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        int currentTimerID = ++timerID;
        TextComponentString text = new TextComponentString("Der Timer ");
        text.getStyle().setColor(TextFormatting.AQUA);

        TextComponentString timerIDComponent = new TextComponentString(String.valueOf(currentTimerID));
        timerIDComponent.getStyle().setColor(TextFormatting.RED);

        TextComponentString textEnd = new TextComponentString(" wurde gestartet.");
        text.getStyle().setColor(TextFormatting.AQUA);

        p.sendMessage(text.appendSibling(timerIDComponent).appendSibling(textEnd));

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                TextComponentString text = new TextComponentString("Der Timer ");
                text.getStyle().setColor(TextFormatting.AQUA);

                TextComponentString textMid = new TextComponentString(" ist abgelaufen.");
                textMid.getStyle().setColor(TextFormatting.AQUA);

                TextComponentString secondsComponent = new TextComponentString(seconds + (seconds == 1 ? " Sekunde" : " Sekunden"));
                secondsComponent.getStyle().setColor(TextFormatting.RED);

                TextComponentString textEnd = new TextComponentString(" sind vergangen.");
                textEnd.getStyle().setColor(TextFormatting.AQUA);

                p.sendMessage(text.appendSibling(timerIDComponent).appendSibling(textMid).appendSibling(secondsComponent).appendSibling(textEnd));
            }
        }, TimeUnit.SECONDS.toMillis(seconds));
        return true;
    }
}
