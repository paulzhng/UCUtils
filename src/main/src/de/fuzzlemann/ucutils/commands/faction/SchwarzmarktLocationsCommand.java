package de.fuzzlemann.ucutils.commands.faction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
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
public class SchwarzmarktLocationsCommand implements CommandExecutor {

    private static final List<Map.Entry<String, BlockPos>> BLACK_MARKET_LIST = Lists.newArrayList(
            Maps.immutableEntry("Containerhalle", new BlockPos(-100, 69, -447)),
            Maps.immutableEntry("Lagerhalle", new BlockPos(-70, 69, 529)),
            Maps.immutableEntry("Alte Scheune", new BlockPos(522, 69, 566)),
            Maps.immutableEntry("Frachtschiff", new BlockPos(-436, 69, -12)),
            Maps.immutableEntry("Flughafen", new BlockPos(-78, 64, 637))
    );

    @Override
    @Command(labels = {"schwarzmarktlocations", "schwarzmarktlocs"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        Message.MessageBuilder builder = Message.builder();

        builder.of("» ").color(TextFormatting.GOLD).advance().of("Positionen der möglichen Schwarzmärkte\n").color(TextFormatting.DARK_PURPLE).advance();

        for (Map.Entry<String, BlockPos> entry : BLACK_MARKET_LIST) {
            String blackMarketName = entry.getKey();
            BlockPos blockPos = entry.getValue();

            builder.of("  * " + blackMarketName).color(TextFormatting.GRAY).advance()
                    .of(": ").color(TextFormatting.DARK_GRAY).advance()
                    .of("X: " + blockPos.getX() + " Y: " + blockPos.getY() + " Z: " + blockPos.getZ()).color(TextFormatting.RED).advance()
                    .of(" (").color(TextFormatting.GRAY).advance()
                    .messageParts(NavigationUtil.getRawNavigationMessage(blockPos).getMessageParts())
                    .of(")\n").color(TextFormatting.GRAY).advance();
        }

        p.sendMessage(builder.build().toTextComponent());
        return true;
    }
}
