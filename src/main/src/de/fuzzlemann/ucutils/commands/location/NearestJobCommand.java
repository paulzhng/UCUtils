package de.fuzzlemann.ucutils.commands.location;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.Job;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class NearestJobCommand implements CommandExecutor {

    @Override
    @Command(labels = "nearestjob")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        Map.Entry<Double, Job> nearestJobEntry = ForgeUtils.getNearestObject(Job.values(), Job::getX, Job::getY, Job::getZ);
        int distance = (int) (double) nearestJobEntry.getKey();
        Job job = nearestJobEntry.getValue();

        Message.builder()
                .prefix()
                .of("Der näheste Job zu dir ist ").color(TextFormatting.GRAY).advance()
                .of(job.getName()).color(TextFormatting.BLUE).advance()
                .of(" (").color(TextFormatting.GRAY).advance()
                .of(distance + " Meter").color(TextFormatting.BLUE).advance()
                .of(").").color(TextFormatting.GRAY).advance()
                .newLine()
                .messageParts(NavigationUtil.getNavigationMessage(job.getX(), job.getY(), job.getZ()).getMessageParts())
                .send();
        return true;
    }
}
