package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class RefreshDataCommand implements CommandExecutor {

    @Override
    @Command(labels = "refreshdata")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        Main.loadData();
        p.sendMessage(TextUtils.simpleMessage("Alle Daten wurden erneuert.", TextFormatting.GREEN));
        return true;
    }
}
