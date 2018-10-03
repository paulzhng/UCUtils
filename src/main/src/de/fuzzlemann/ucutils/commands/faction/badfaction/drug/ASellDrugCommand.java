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
    @Command(labels = "aselldrug", usage = "/%label% [Spieler] [Droge] [Menge] (Variation)")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 3) return false;

        int variation;

        if (args.length == 4) {
            try {
                variation = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            variation = 0;
        }

        Drug drug = DrugUtil.getDrug(args[1]);
        if (drug == null) {
            TextUtils.error("Die Droge wurde nicht gefunden.");
            return true;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        int price = amount * (drug.getPrice() + variation);
        p.sendChatMessage("/selldrug " + args[0] + " " + drug.getName() + " " + amount + " " + price);
        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Collections.emptyList();

        String drug = args[args.length - 1].toLowerCase();
        List<String> drugNames = DrugUtil.DRUGS
                .stream()
                .map(Drug::getName)
                .collect(Collectors.toList());

        if (drug.isEmpty()) return drugNames;

        drugNames.removeIf(drugName -> !drugName.toLowerCase().startsWith(drug));

        Collections.sort(drugNames);
        return drugNames;
    }
}