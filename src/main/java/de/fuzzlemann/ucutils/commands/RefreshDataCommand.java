package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.data.DataManager;
import de.fuzzlemann.ucutils.base.text.Message;
import net.minecraft.util.text.TextFormatting;

import java.util.Collections;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class RefreshDataCommand implements TabCompletion {

    @Command("refreshdata")
    public boolean onCommand(@CommandParam(required = false, requiredValue = "-v") boolean verbose) {
        Main.unifiedDataFetcher.reload();

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

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.singletonList("-v");

        return null;
    }
}
