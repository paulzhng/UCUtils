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

        Message.builder()
                .prefix()
                .of(playerName).color(TextFormatting.BLUE).advance()
                .of(" besitzt " + (hasMedicalLicense ? "eine" : "kein") + " medizinische Marihuanalizenz.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}