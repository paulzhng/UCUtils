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
public class GiveDrugCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(labels = "givedrug", usage = "/%label% [Spieler] [Droge] [Menge] [-m]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 3) return false;

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

        if (args.length > 3 && args[3].equalsIgnoreCase("-m"))
            p.sendChatMessage("/pay " + args[0] + " 1");

        p.sendChatMessage("/selldrug " + args[0] + " " + drug.getName() + " " + amount + " " + 1);
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
        return drugNames;
    }
}