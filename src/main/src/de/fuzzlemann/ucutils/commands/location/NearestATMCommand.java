package de.fuzzlemann.ucutils.commands.location;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.ATM;
import de.fuzzlemann.ucutils.utils.location.NavigationUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class NearestATMCommand implements CommandExecutor {

    @Override
    @Command(labels = "nearestatm")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (ATM.values().length == 0) {
            return false;
        }

        new Thread(() -> {
            BlockPos pos = p.getPosition();

            ATM nearestATM = null;
            double nearestDistance = -1;
            for (ATM atm : ATM.values()) {
                double distance = pos.getDistance(atm.getX(), atm.getY(), atm.getZ());

                if (distance < nearestDistance || nearestDistance == -1) {
                    nearestDistance = distance;
                    nearestATM = atm;
                }
            }

            assert nearestATM != null;

            TextComponentString text = new TextComponentString("Der n\u00e4heste ATM an dir ist ");
            text.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString atmComponent = new TextComponentString("ATM " + nearestATM.getId());
            atmComponent.getStyle().setColor(TextFormatting.RED);

            TextComponentString blockComponent = new TextComponentString(". Die Distanz zu dem ATM betr\u00e4gt ");
            blockComponent.getStyle().setColor(TextFormatting.AQUA);

            TextComponentString distanceComponent = new TextComponentString((int) nearestDistance + " Meter");
            distanceComponent.getStyle().setColor(TextFormatting.RED);

            TextComponentString textEnd = new TextComponentString(".");
            textEnd.getStyle().setColor(TextFormatting.AQUA);

            ITextComponent navigateThereComponent = new TextComponentString("\n").appendSibling(NavigationUtil.getNavigationText(nearestATM.getX(), nearestATM.getY(), nearestATM.getZ()));

            p.sendMessage(text.appendSibling(atmComponent).appendSibling(blockComponent).appendSibling(distanceComponent).appendSibling(textEnd).appendSibling(navigateThereComponent));
        }).start();

        return true;
    }
}
