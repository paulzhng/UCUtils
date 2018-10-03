package de.fuzzlemann.ucutils.commands.location;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.Job;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
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

            TextComponentString text = new TextComponentString("Der näheste Job an dir ist ");
            text.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString jobComponent = new TextComponentString(nearestJob.getName());
            jobComponent.getStyle().setColor(TextFormatting.RED);

            TextComponentString blockComponent = new TextComponentString(". Die Distanz zu dem Job beträgt ");
            blockComponent.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString distanceComponent = new TextComponentString((int) nearestDistance + " Meter");
            distanceComponent.getStyle().setColor(TextFormatting.RED);

            TextComponentString textEnd = new TextComponentString(".");
            textEnd.getStyle().setColor(TextFormatting.AQUA);

            ITextComponent navigateThereComponent = new TextComponentString("\n").appendSibling(NavigationUtil.getNavigationText(nearestJob.getX(), nearestJob.getY(), nearestJob.getZ()));

            p.sendMessage(text.appendSibling(jobComponent).appendSibling(blockComponent).appendSibling(distanceComponent).appendSibling(textEnd).appendSibling(navigateThereComponent));
        }).start();

        return true;
    }
}
