package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.data.DataManager;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class RefreshDataCommand {

    @Command("refreshdata")
    public boolean onCommand(@CommandParam(required = false, requiredValue = "-v") boolean verbose) {
        if (verbose) {
            DataManager.loadData(true);
        } else {
            DataManager.loadData(false);

            Message.builder()
                    .prefix()
                    .of("Einige Daten wurden neu geladen.").color(TextFormatting.GRAY).advance()
                    .newLine()
                    .info()
                    .of("Für mehr Informationen, führ ").color(TextFormatting.WHITE).advance()
                    .of("/").color(TextFormatting.DARK_GRAY).advance()
                    .of("refreshdata -v").color(TextFormatting.GOLD).advance()
                    .of(" aus.").color(TextFormatting.WHITE).advance()
                    .send();
        }
        return true;
    }
}