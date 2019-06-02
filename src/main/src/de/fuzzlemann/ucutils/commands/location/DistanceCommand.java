package de.fuzzlemann.ucutils.commands.location;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
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

        if (y == -1) y = (int) p.posY;

        int distance = (int) p.getPosition().getDistance(x, y, z);

        Message.builder()
                .prefix()
                .of("Du bist ").color(TextFormatting.GRAY).advance()
                .of(distance + " Meter").color(TextFormatting.BLUE).advance()
                .of(" von den Koordinaten ").color(TextFormatting.GRAY).advance()
                .of(x + "/" + y + "/" + z).color(TextFormatting.BLUE).advance()
                .of(" entfernt.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}
