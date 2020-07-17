package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugQuality;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class GiveDrugCommand implements TabCompletion {

    @Command(value = "givedrug", usage = "/%label% [Spieler] [Droge] [Qualität] [Menge] [-m]", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(UPlayer p, String target, DrugType drugType, DrugQuality drugQuality, int amount, @CommandParam(required = false, requiredValue = "-m") boolean giveMoney) {
        if (giveMoney)
            p.sendChatMessage("/pay " + target + " 1");

        p.sendChatMessage("/selldrug " + target + " " + drugType.getName() + " " + drugQuality.getId() + " " + amount);
        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.emptyList();
        if (args.length == 2) {
            return Arrays.stream(DrugType.values())
                    .map(DrugType::getName)
                    .collect(Collectors.toList());
        }
        if (args.length == 3) {
            return Arrays.stream(DrugQuality.values())
                    .map(DrugQuality::getName)
                    .collect(Collectors.toList());
        }
        if (args.length == 5) return Collections.singletonList("-m");

        return null;
    }
}