package de.fuzzlemann.ucutils.commands.job;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.job.Job;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class NearestJobCommand implements CommandExecutor {

    @Override
    @Command(labels = "nearestjob")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (Job.values().length == 0) {
            return false;
        }

        new Thread(() -> {
            BlockPos pos = p.getPosition();

            Job nearestJob = null;
            double nearestDistance = -1;
            for (Job job : Job.values()) {
                double distance = pos.getDistance(job.getX(), job.getY(), job.getZ());

                if (distance < nearestDistance || nearestDistance == -1) {
                    nearestDistance = distance;
                    nearestJob = job;
                }
            }

            assert nearestJob != null;

            TextComponentString text = new TextComponentString("Der n\u00e4heste Job an dir ist ");
            text.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString jobComponent = new TextComponentString(nearestJob.getName());
            jobComponent.getStyle().setColor(TextFormatting.RED);

            TextComponentString blockComponent = new TextComponentString(". Die Distanz zu dem Job betr\u00e4gt ");
            blockComponent.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString distanceComponent = new TextComponentString((int) nearestDistance + " Meter");
            distanceComponent.getStyle().setColor(TextFormatting.RED);

            TextComponentString textEnd = new TextComponentString(".");
            textEnd.getStyle().setColor(TextFormatting.AQUA);

            p.sendMessage(text.appendSibling(jobComponent).appendSibling(blockComponent).appendSibling(distanceComponent).appendSibling(textEnd));
        }).start();

        return true;
    }
}
