package de.fuzzlemann.ucutils.commands.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist.BlacklistUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class BlacklistReasonsCommand {

    @Command(value = {"blacklistreasons", "blreasons"})
    public boolean onCommand() {
        Message.builder()
                .of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Blacklistgründe der Fraktion\n").color(TextFormatting.DARK_AQUA).advance()
                .joiner(BlacklistUtil.BLACKLIST_REASONS)
                .consumer((b, blacklistReason) -> b.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                        .of(blacklistReason.getReason()).color(TextFormatting.GRAY).advance()
                        .of(": ").color(TextFormatting.DARK_GRAY).advance()
                        .of(blacklistReason.getAmount() + "$").color(TextFormatting.BLUE).advance()
                        .of(" & ").color(TextFormatting.DARK_GRAY).advance()
                        .of(blacklistReason.getKills() + " Kills").color(TextFormatting.BLUE).advance())
                .newLineJoiner()
                .advance()
                .send();

        return true;
    }
}
