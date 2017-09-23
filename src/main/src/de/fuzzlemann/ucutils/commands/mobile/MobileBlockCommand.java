package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MobileBlockCommand implements CommandExecutor {

    @Override
    @Command(labels = {"mobileblock", "toggleblock", "block"}, usage = "/%label% [Spielername]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String block = args[0];
        if (MobileUtils.isBlocked(block)) {
            MobileUtils.unblock(block);

            TextComponentString text = new TextComponentString("Du hast ");
            text.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString blocked = new TextComponentString(block);
            blocked.getStyle().setColor(TextFormatting.RED);

            TextComponentString textEnd = new TextComponentString(" entblockt.");
            textEnd.getStyle().setColor(TextFormatting.AQUA);

            p.sendMessage(text.appendSibling(blocked).appendSibling(textEnd));
        } else {
            MobileUtils.block(block);

            TextComponentString text = new TextComponentString("Du hast ");
            text.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString blocked = new TextComponentString(block);
            blocked.getStyle().setColor(TextFormatting.RED);

            TextComponentString textEnd = new TextComponentString(" blockiert.");
            textEnd.getStyle().setColor(TextFormatting.AQUA);

            p.sendMessage(text.appendSibling(blocked).appendSibling(textEnd));
        }
        return true;
    }
}
