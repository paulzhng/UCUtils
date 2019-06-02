package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
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

            Message.builder()
                    .prefix()
                    .of("Du hast ").color(TextFormatting.GRAY).advance()
                    .of(block).color(TextFormatting.BLUE).advance()
                    .of(" entblockt.").color(TextFormatting.GRAY).advance()
                    .send();
        } else {
            MobileUtils.block(block);

            Message.builder()
                    .prefix()
                    .of("Du hast ").color(TextFormatting.GRAY).advance()
                    .of(block).color(TextFormatting.BLUE).advance()
                    .of(" blockiert.").color(TextFormatting.GRAY).advance()
                    .send();
        }
        return true;
    }
}
