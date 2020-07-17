package de.fuzzlemann.ucutils.commands.faction.badfaction.drug;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugQuality;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugType;
import de.fuzzlemann.ucutils.utils.faction.badfaction.drug.DrugUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class DrugPriceCommand {

    @Command(value = {"drugprice", "drugprices"})
    public boolean onCommand() {
        Message.builder()
                .of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Drogenpreise der Fraktion\n").color(TextFormatting.DARK_AQUA).advance()
                .joiner(DrugUtil.drugPrice.getPrices())
                .consumer((b, drugPriceEntry) -> {
                    DrugType drugType = drugPriceEntry.getDrugType();
                    DrugQuality drugQuality = drugPriceEntry.getDrugQuality();
                    int price = drugPriceEntry.getMoney();

                    b.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                            .of(drugType.getName() + " (" + drugQuality.getName() + "): ").color(TextFormatting.GRAY).advance()
                            .of(price + "$").color(TextFormatting.BLUE).advance();
                })
                .newLineJoiner()
                .advance()
                .send();

        return true;
    }
}
