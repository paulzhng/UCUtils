package de.fuzzlemann.ucutils.commands.faction.badfaction;

import com.google.common.collect.Lists;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class SchwarzmarktLocationsCommand {

    private static final List<Map.Entry<String, BlockPos>> BLACK_MARKET_LIST = Lists.newArrayList(

    );

    @Command({"schwarzmarktlocations", "schwarzmarktlocs"})
    public boolean onCommand() {
        TextUtils.error("Temporär deaktiviert; viel Spaß beim Suchen! ;)"); //todo
        /*Message.builder()
                .of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Positionen aller möglichen Schwarzmärkte\n").color(TextFormatting.DARK_AQUA).advance()
                .joiner(BLACK_MARKET_LIST)
                .consumer((b, entry) -> {
                    String name = entry.getKey();
                    BlockPos blockPos = entry.getValue();

                    b.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                            .of(name + ": ").color(TextFormatting.GRAY).advance()
                            .messageParts(NavigationUtil.getRawNavigationMessage(blockPos).getMessageParts());
                }).newLineJoiner().advance()
                .send();*/
        return true;
    }
}
