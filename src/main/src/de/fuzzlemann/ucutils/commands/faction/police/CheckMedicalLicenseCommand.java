package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.faction.police.MedicalLicenseHandler;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Fuzzlemann
 */
public class CheckMedicalLicenseCommand implements CommandExecutor {

    @Override
    @Command(labels = {"checkmedicallicense", "cml"}, usage = "/%label% [Spieler]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String playerName = args[0];
        boolean hasMedicalLicense = MedicalLicenseHandler.hasMedicalLicense(playerName);

        Message.MessageBuilder builder = Message.builder();

        builder.of(playerName + " besitzt ").color(TextFormatting.AQUA).advance();
        builder.of(hasMedicalLicense ? "eine" : "keine").color(hasMedicalLicense ? TextFormatting.GREEN : TextFormatting.RED).advance();
        builder.of(" medizinische Marihuanalizenz.").color(TextFormatting.AQUA).advance();

        p.sendMessage(builder.build().toTextComponent());
        return true;
    }
}