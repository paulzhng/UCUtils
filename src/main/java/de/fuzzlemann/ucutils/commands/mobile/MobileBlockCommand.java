package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import de.fuzzlemann.ucutils.base.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MobileBlockCommand {

    @Command(value = {"mobileblock", "toggleblock", "block"}, usage = "/%label% [Spielername]")
    public boolean onCommand(String target) {
        if (MobileUtils.isBlocked(target)) {
            MobileUtils.unblock(target);

            Message.builder()
                    .prefix()
                    .of("Du hast ").color(TextFormatting.GRAY).advance()
                    .of(target).color(TextFormatting.BLUE).advance()
                    .of(" entblockt.").color(TextFormatting.GRAY).advance()
                    .send();
        } else {
            MobileUtils.block(target);

            Message.builder()
                    .prefix()
                    .of("Du hast ").color(TextFormatting.GRAY).advance()
                    .of(target).color(TextFormatting.BLUE).advance()
                    .of(" blockiert.").color(TextFormatting.GRAY).advance()
                    .send();
        }
        return true;
    }
}
