package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.Drug;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugUtil;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.TextUtils;
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
public class DrugPriceCommand implements TabCompletion {

    @Command(value = {"drugprice", "drugprices"}, usage = "/%label% [list/setprice] (Droge) (Preis)", sendUsageOn = NumberFormatException.class)
    public boolean onCommand(String argument,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Drug drug,
                             @CommandParam(required = false, defaultValue = CommandParam.NULL) Integer price) {
        switch (argument.toLowerCase()) {
            case "list":
                sendDrugPrices();
                break;
            case "setprice":
                if (price == null) return false;
                if (drug == null) {
                    TextUtils.error("Die Droge wurde nicht gefunden.");
                    return true;
                }

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
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length != 2) return Arrays.asList("setprice", "list");
        if (!args[0].equalsIgnoreCase("setprice")) return null;

        return DrugUtil.DRUGS
                .stream()
                .map(Drug::getName)
                .collect(Collectors.toList());
    }

    private void sendDrugPrices() {
        Message.builder()
                .of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Eingestellte Drogenpreise\n").color(TextFormatting.DARK_AQUA).advance()
                .joiner(DrugUtil.DRUGS)
                .consumer((b, drug) -> b.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                        .of(drug.getName() + ": ").color(TextFormatting.GRAY).advance()
                        .of(drug.getPrice() + "$").color(TextFormatting.BLUE).advance())
                .newLineJoiner()
                .advance()
                .send();
    }
}
