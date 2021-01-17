package de.fuzzlemann.ucutils.commands.faction.badfaction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
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
            Maps.immutableEntry("Brücke (Hausaddonshop)", new BlockPos(80, 58, -118)),
            Maps.immutableEntry("Flughafen (UCA)", new BlockPos(-78, 63, 637)),
            Maps.immutableEntry("Hafen", new BlockPos(-405, 69, 29)),
            Maps.immutableEntry("Containerhalle", new BlockPos(-93, 69, -438)),
            Maps.immutableEntry("Strand (Mex)", new BlockPos(-524, 66, -236)),
            Maps.immutableEntry("U-Bahn (Kerzakov-Gebiet)", new BlockPos(849, 52, 262)),
            Maps.immutableEntry("Yachthafen", new BlockPos(285, 63, -635)),
            Maps.immutableEntry("Uran Berg", new BlockPos(-437, 167, 800)),
            Maps.immutableEntry("Mühle (Chinatown)", new BlockPos(1225, 68, 198)),
            Maps.immutableEntry("Tanzfläche (Las-Unicas)", new BlockPos(1341, 64, 355)),
            Maps.immutableEntry("Schwimmbad (Las-Unicas)", new BlockPos(1647, 52, 241))
    );

    @Command({"schwarzmarktlocations", "schwarzmarktlocs", "smarktlocs"})
    public boolean onCommand() {
        Message.builder()
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
                .send();
        return true;
    }
}
