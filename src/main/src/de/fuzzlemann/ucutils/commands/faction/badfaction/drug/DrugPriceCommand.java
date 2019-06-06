package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.Drug;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class DrugPriceCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(value = {"drugprice", "drugprices"}, usage = "/%label% [list/setprice] (Droge) (Preis)", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        switch (args[0].toLowerCase()) {
            case "list":
                sendDrugPrices();
                break;
            case "setprice":
                if (args.length < 3) return false;

                Drug drug = DrugUtil.getDrug(args[1]);
                if (drug == null) {
                    TextUtils.error("Die Droge wurde nicht gefunden.");
                    return true;
                }

                int price = Integer.parseInt(args[2]);

                drug.setPrice(price);
                DrugUtil.savePrices();

                Message.builder()
                        .prefix()
                        .of("Du hast den Preis von ").color(TextFormatting.GRAY).advance()
                        .of(drug.getName()).color(TextFormatting.BLUE).advance()
                        .of(" zu ").color(TextFormatting.GRAY).advance()
                        .of(price + "$").color(TextFormatting.BLUE).advance()
                        .of(" geändert.").color(TextFormatting.GRAY).advance()
                        .send();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Arrays.asList("setprice", "list");
        if (!args[0].equalsIgnoreCase("setprice")) return null;

        return DrugUtil.DRUGS
                .stream()
                .map(Drug::getName)
                .collect(Collectors.toList());
    }

    private void sendDrugPrices() {
        Message.MessageBuilder builder = Message.builder();

        builder.of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Eingestellte Drogenpreise\n").color(TextFormatting.DARK_AQUA).advance();
        for (Drug drug : DrugUtil.DRUGS) {
            builder.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                    .of(drug.getName() + ": ").color(TextFormatting.GRAY).advance()
                    .of(drug.getPrice() + "$\n").color(TextFormatting.BLUE).advance();
        }

        builder.send();
    }
}
