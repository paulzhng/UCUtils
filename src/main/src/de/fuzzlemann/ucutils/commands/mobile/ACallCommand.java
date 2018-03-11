package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ACallCommand implements CommandExecutor {

    @Override
    @Command(labels = "acall", usage = "/%label% [Spieler]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        new Thread(() -> {
            String player = args[0];

            int number = MobileUtils.getNumber(p, player);
            if (number == -1) return;

            p.sendChatMessage("/call " + number);
        }).start();
        return true;
    }
}
