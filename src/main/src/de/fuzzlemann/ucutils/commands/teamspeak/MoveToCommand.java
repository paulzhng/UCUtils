package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MoveToCommand implements CommandExecutor {

    @Override
    @Command(labels = "moveto", usage = "/%label% [Spieler]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        new Thread(() -> moveTo(args[0])).start();
        return true;
    }

    private static void moveTo(String name) {
        List<Map<String, String>> clients = TSUtils.getClientsByName(name);
        if (clients.isEmpty()) {
            TextUtils.error("Es wurde kein Spieler auf dem TeamSpeak mit diesem Namen gefunden.");
            return;
        }

        Map<String, String> client = clients.get(0);
        String channelID = client.get("cid");

        TSClientQuery.exec("clientmove cid=" + channelID + " clid=0");
        Main.MINECRAFT.player.sendMessage(TextUtils.simpleMessage("Die Aktion wurde erfolgreich ausgef\u00fchrt.", TextFormatting.GREEN));
    }
}
