package de.fuzzlemann.ucutils.commands.faction;

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
    @Command(labels = {"checkhouseban", "chb"}, usage = "/%label% [Spieler]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String playerName = args[0];
        boolean hasHouseBan = hasHouseBan(playerName);

        Message.MessageBuilder builder = Message.builder();

        builder.of(playerName + " besitzt ").color(TextFormatting.AQUA).advance()
                .of(hasHouseBan ? "ein" : "kein").color(hasHouseBan ? TextFormatting.GREEN : TextFormatting.RED).advance()
                .of(" Hausverbot.").color(TextFormatting.AQUA).advance();

        p.sendMessage(builder.build().toTextComponent());
        return true;
    }

    private boolean hasHouseBan(String playerName) {
        try {
            String response = APIUtils.post("http://tomcat.fuzzlemann.de/factiononline/checkhouseban", "name", playerName);
            return Boolean.valueOf(response);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}