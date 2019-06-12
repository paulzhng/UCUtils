package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MoveToCommand {

    @Command(value = "moveto", usage = "/%label% [Spieler]", async = true)
    public boolean onCommand(String name) {
        moveTo(name, MojangAPI.getEarlierNames(name));
        return true;
    }

    private static void moveTo(String name, List<String> names) {
        List<Map<String, String>> clients = TSUtils.getClientsByName(names);
        if (clients.isEmpty()) {
            TextUtils.error("Es wurde kein Spieler auf dem TeamSpeak mit diesem Namen gefunden.");
            return;
        }

        Map<String, String> client = clients.get(0);
        String channelID = client.get("cid");

        TSClientQuery.exec("clientmove cid=" + channelID + " clid=0");

        Message.builder()
                .prefix()
                .of("Du hast dich zu dem Channel von ").color(TextFormatting.GRAY).advance()
                .of(name).color(TextFormatting.BLUE).advance()
                .of(" bewegt.").color(TextFormatting.GRAY).advance()
                .send();
    }
}