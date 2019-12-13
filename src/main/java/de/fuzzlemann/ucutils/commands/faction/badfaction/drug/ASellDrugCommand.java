package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
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
public class ASellDrugCommand implements TabCompletion {

    @Command(value = "aselldrug", usage = "/%label% [Spieler] [Droge] [Menge] (Variation)", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(UPlayer p, String target, Drug drug, int amount, @CommandParam(required = false, defaultValue = "0") int variation) {
        int price = amount * (drug.getPrice() + variation);
        p.sendChatMessage("/selldrug " + target + " " + drug.getName() + " " + amount + " " + price);
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

        return null;
    }
}