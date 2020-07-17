package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.faction.police.MedicalLicenseHandler;
import de.fuzzlemann.ucutils.base.text.Message;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Fuzzlemann
 */
public class CheckMedicalLicenseCommand {

    @Command(value = {"checkmedicallicense", "cml"}, usage = "/%label% [Spieler]", async = true)
    public boolean onCommand(String target) {
        boolean hasMedicalLicense = MedicalLicenseHandler.hasMedicalLicense(target);

        Message.builder()
                .prefix()
                .of(target).color(TextFormatting.BLUE).advance()
                .of(" besitzt " + (hasMedicalLicense ? "eine" : "kein") + " medizinische Marihuanalizenz.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}