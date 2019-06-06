package de.fuzzlemann.ucutils.commands.time;

import de.fuzzlemann.ucutils.utils.FormatUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class StopWatchCommand implements CommandExecutor {

    private long start = -1;

    @Override
    @Command({"stopwatch", "stoppuhr"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (start == -1) {
            start = System.currentTimeMillis();

            TextUtils.simplePrefixMessage("Du hast angefangen, die Zeit zu stoppen.");
        } else {
            long difference = System.currentTimeMillis() - start;
            start = -1;

            Message.builder()
                    .prefix()
                    .of("Die gestoppte Zeit beträgt: ").color(TextFormatting.GRAY).advance()
                    .messageParts(FormatUtils.formatMillisecondsToMessage(difference))
                    .of(".").color(TextFormatting.GRAY).advance()
                    .send();
        }

        return true;
    }
}