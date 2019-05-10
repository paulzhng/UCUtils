package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MoveCommand implements CommandExecutor {

    @Override
    @Command(labels = "move", usage = "/%label% [Spieler...] [Ziel]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        new Thread(() -> {
            List<String> moved = new ArrayList<>();
            for (int i = 0; i < args.length - 1; i++) {
                moved.addAll(MojangAPI.getEarlierNames(args[i]));
            }

            move(moved, MojangAPI.getEarlierNames(args[args.length - 1]));
        }).start();
        return true;
    }

    private static void move(List<String> moved, List<String> moveTo) {
        List<Map<String, String>> clientsMoved = TSUtils.getClientsByName(moved);
        List<Map<String, String>> clientsMoveTo = TSUtils.getClientsByName(moveTo);

        if (clientsMoved.isEmpty() || clientsMoveTo.isEmpty()) {
            TextUtils.error("Einer der Spieler befindet sich nicht auf dem TeamSpeak.");
            return;
        }

        StringJoiner stringJoiner = new StringJoiner("|");
        for (Map<String, String> client : clientsMoved) {
            stringJoiner.add("clid=" + client.get("clid"));
        }

        Map<String, String> clientMoveTo = clientsMoveTo.get(0);
        String channelID = clientMoveTo.get("cid");

        TSClientQuery.exec("clientmove cid=" + channelID + " " + stringJoiner);

        Main.MINECRAFT.player.sendMessage(TextUtils.simpleMessage("Die Aktion wurde erfolgreich ausgeführt.", TextFormatting.GREEN));
    }
}
