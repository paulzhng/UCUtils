package de.fuzzlemann.ucutils.commands.location;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.ATM;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class NearestATMCommand implements CommandExecutor {

    @Override
    @Command(labels = "nearestatm")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        Map.Entry<Double, ATM> nearestATMEntry = ForgeUtils.getNearestObject(ATM.values(), ATM::getX, ATM::getY, ATM::getZ);
        int distance = (int) (double) nearestATMEntry.getKey();
        ATM atm = nearestATMEntry.getValue();

        Message.builder()
                .prefix()
                .of("Der näheste ATM zu dir ist ").color(TextFormatting.GRAY).advance()
                .of("ATM " + atm.getID()).color(TextFormatting.BLUE).advance()
                .of(" (").color(TextFormatting.GRAY).advance()
                .of(distance + " Meter").color(TextFormatting.BLUE).advance()
                .of(").").color(TextFormatting.GRAY).advance()
                .newLine()
                .messageParts(NavigationUtil.getNavigationMessage(atm.getX(), atm.getY(), atm.getZ()).getMessageParts())
                .send();
        return true;
    }
}
