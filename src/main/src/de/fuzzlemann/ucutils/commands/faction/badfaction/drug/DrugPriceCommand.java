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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class DrugPriceCommand implements CommandExecutor, TabCompletion {

    @Override
    @Command(labels = {"drugprice", "drugprices"}, usage = "/%label% [list/setprice] (Droge) (Preis)")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        switch (args[0].toLowerCase()) {
            case "list":
                sendDrugPrices(p);
                break;
            case "setprice":
                if (args.length < 3) return false;

                Drug drug = DrugUtil.getDrug(args[1]);
                if (drug == null) {
                    p.sendMessage(TextUtils.simpleMessage("Die Droge wurde nicht gefunden.", TextFormatting.RED));
                    return true;
                }

                int price;
                try {
                    price = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    return false;
                }

                drug.setPrice(price);
                DrugUtil.savePrices();

                p.sendMessage(Message.builder().of("Du hast den Preis von ").color(TextFormatting.AQUA).advance()
                        .of(drug.getName()).color(TextFormatting.RED).advance()
                        .of(" zu ").color(TextFormatting.AQUA).advance()
                        .of(String.valueOf(price) + "$ ").color(TextFormatting.RED).advance()
                        .of("ge\u00e4ndert.").color(TextFormatting.AQUA).advance().build().toTextComponent());
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Arrays.asList("setprice", "list");
        if (!args[0].equalsIgnoreCase("setprice")) return Collections.emptyList();

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

    private void sendDrugPrices(EntityPlayerSP p) {
        Message.MessageBuilder builder = Message.builder();

        builder.of("\u00bb ").color(TextFormatting.GOLD).advance().of("Eingestellte Drogenpreise\n").color(TextFormatting.DARK_PURPLE).advance();
        for (Drug drug : DrugUtil.DRUGS) {
            builder.of("  * " + drug.getName()).color(TextFormatting.GRAY).advance()
                    .of(": ").color(TextFormatting.DARK_GRAY).advance()
                    .of(drug.getPrice() + "$\n").color(TextFormatting.RED).advance();
        }

        p.sendMessage(builder.build().toTextComponent());
    }
}
