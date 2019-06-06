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
    @Command(value = "givedrug", usage = "/%label% [Spieler] [Droge] [Menge] [-m]", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 3) return false;

        Drug drug = DrugUtil.getDrug(args[1]);
        if (drug == null) {
            TextUtils.error("Die Droge wurde nicht gefunden.");
            return true;
        }

        int amount = Integer.parseInt(args[2]);

        if (args.length > 3 && args[3].equalsIgnoreCase("-m"))
            p.sendChatMessage("/pay " + args[0] + " 1");

        p.sendChatMessage("/selldrug " + args[0] + " " + drug.getName() + " " + amount + " " + 1);
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
        if (args.length == 4) return Collections.singletonList("-m");

        return null;
    }
}