package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASMSCommand implements CommandExecutor {

    @Override
    @Command(labels = "asms", usage = "/%label% [Spieler] [Nachricht]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        new Thread(() -> {
            String player = args[0];
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            int number = 0;
            try {
                number = MobileUtils.getNumber(p, player).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (number == -1) return;

            p.sendChatMessage("/sms " + number + " " + message);
        }).start();

        return true;
    }
}
