package de.fuzzlemann.ucutils.commands.time;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ClockCommand {

    @Command({"clock", "uhrzeit", "uhr"})
    public boolean onCommand() {
        sendClockMessage();
        return true;
    }

    public static void sendClockMessage() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.GERMAN);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy", Locale.GERMAN);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss 'Uhr'");

        Date date = new Date();

        String dayString = dayFormat.format(date);
        String dateString = dateFormat.format(date);
        String timeString = timeFormat.format(date);

        Message.builder()
                .prefix()
                .of("Heute ist ").color(TextFormatting.GRAY).advance()
                .of(dayString).color(TextFormatting.BLUE).advance()
                .of(", der ").color(TextFormatting.GRAY).advance()
                .of(dateString).color(TextFormatting.BLUE).advance()
                .of(" und wir haben ").color(TextFormatting.GRAY).advance()
                .of(timeString).color(TextFormatting.BLUE).advance()
                .of(".").color(TextFormatting.GRAY).advance()
                .send();
    }
}