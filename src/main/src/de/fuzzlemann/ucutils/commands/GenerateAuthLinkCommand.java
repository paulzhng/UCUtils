package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

/**
 * @author Fuzzlemann
 */
public class GenerateAuthLinkCommand implements CommandExecutor {

    @Override
    @Command(value = "generateauthlink", async = true)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        String authKey = APIUtils.generateAuthKey();
        if (authKey == null) {
            TextUtils.error("Ein Fehler ist aufgetreten, bitte registriere dich manuell.");
            return true;
        }

        Message.builder()
                .prefix()
                .of("Du kannst dich unter dem Link ").color(TextFormatting.GRAY).advance()
                .of("https://tomcat.fuzzlemann.de/factiononline/register?authkey=" + authKey)
                .clickEvent(ClickEvent.Action.OPEN_URL, "https://tomcat.fuzzlemann.de/factiononline/register?authkey=" + authKey)
                .color(TextFormatting.BLUE).advance()
                .of(" registrieren.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}
