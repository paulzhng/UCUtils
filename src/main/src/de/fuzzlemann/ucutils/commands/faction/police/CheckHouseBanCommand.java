package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Fuzzlemann
 */
public class CheckHouseBanCommand implements CommandExecutor {

    @Override
    @Command(value = {"checkhouseban", "chb"}, usage = "/%label% [Spieler]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String playerName = args[0];
        boolean hasHouseBan = hasHouseBan(playerName);

        Message.builder()
                .prefix()
                .of(playerName).color(TextFormatting.BLUE).advance()
                .of(" besitzt " + (hasHouseBan ? "ein" : "kein") + " Hausverbot.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }

    private boolean hasHouseBan(String playerName) {
        try {
            String response = APIUtils.post("http://tomcat.fuzzlemann.de/factiononline/checkhouseban", "name", playerName);
            return Boolean.parseBoolean(response);
        } catch (Exception e) {
            Logger.LOGGER.catching(e);
            return false;
        }
    }
}