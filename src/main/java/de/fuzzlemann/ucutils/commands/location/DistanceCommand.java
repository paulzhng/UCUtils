package de.fuzzlemann.ucutils.commands.location;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class DistanceCommand {

    @Command(value = "distance", usage = "/%label% [X] [Y] [Z]")
    public boolean onCommand(UPlayer p, int x, int y, int z) {
        if (y == -1) y = p.getPosition().getY();
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
