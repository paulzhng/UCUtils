package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.Drug;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugUtil;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASellDrugCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(value = "aselldrug", usage = "/%label% [Spieler] [Droge] [Menge] (Variation)", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 3) return false;

        int variation;

        if (args.length == 4) {
            variation = Integer.parseInt(args[3]);
        } else {
            variation = 0;
        }

        Drug drug = DrugUtil.getDrug(args[1]);
        if (drug == null) {
            TextUtils.error("Die Droge wurde nicht gefunden.");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        int price = amount * (drug.getPrice() + variation);
        p.sendChatMessage("/selldrug " + args[0] + " " + drug.getName() + " " + amount + " " + price);
        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
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