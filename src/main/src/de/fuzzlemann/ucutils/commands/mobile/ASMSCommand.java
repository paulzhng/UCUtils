package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASMSCommand implements CommandExecutor {

    @Override
    @Command(value = "asms", usage = "/%label% [Spieler] [Nachricht]", async = true)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        String player = args[0];
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        int number = MobileUtils.getNumber(p, player);
        if (number == -1) return true;

        p.sendChatMessage("/sms " + number + " " + message);
        return true;
    }
}
