package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.concurrent.ExecutionException;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ACallCommand implements CommandExecutor {

    @Override
    @Command(labels = "acall", usage = "/%label% [Spieler]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        new Thread(() -> {
            String player = args[0];

            int number = 0;
            try {
                number = MobileUtils.getNumber(p, player).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (number == -1) return;

            p.sendChatMessage("/call " + number);
        }).start();
        return true;
    }
}
