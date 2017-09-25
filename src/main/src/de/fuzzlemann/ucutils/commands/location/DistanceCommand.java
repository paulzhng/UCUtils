package de.fuzzlemann.ucutils.commands.location;

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
public class DistanceCommand implements CommandExecutor {

    @Override
    @Command(labels = "distance", usage = "/%label% [X] [Y] [Z]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 3) return false;

        int x;
        int y;
        int z;

        try {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (x == -1) {
            x = (int) p.posX;
        }

        int distance = (int) p.getPosition().getDistance(x, y, z);

        TextComponentString text = new TextComponentString("Die Distanz zu der Position betr\u00e4gt: ");
        text.getStyle().setColor(TextFormatting.AQUA);

        TextComponentString distanceComponent = new TextComponentString(distance + " Meter");
        distanceComponent.getStyle().setColor(TextFormatting.RED);

        p.sendMessage(text.appendSibling(distanceComponent));
        return true;
    }
}
