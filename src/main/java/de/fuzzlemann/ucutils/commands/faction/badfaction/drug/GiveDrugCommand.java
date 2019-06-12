package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.command.api.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.Drug;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class GiveDrugCommand implements TabCompletion {

    @Command(value = "givedrug", usage = "/%label% [Spieler] [Droge] [Menge] [-m]", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(UPlayer p, String target, Drug drug, int amount, @CommandParam(required = false, requiredValue = "-m") boolean giveMoney) {
        if (giveMoney)
            p.sendChatMessage("/pay " + target + " 1");

        p.sendChatMessage("/selldrug " + target + " " + drug.getName() + " " + amount + " " + 1);
        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.emptyList();
        if (args.length == 2) {
            return DrugUtil.DRUGS
                    .stream()
                    .map(Drug::getName)
                    .collect(Collectors.toList());
        }
        if (args.length == 4) return Collections.singletonList("-m");

        return null;
    }
}