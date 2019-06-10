package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
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
public class MoveCommand {

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

        TextUtils.simpleMessage("Du hast die Personen gemoved.");
    }

    @Command(value = "move", usage = "/%label% [Spieler...] [Ziel]", async = true)
    public boolean onCommand(@CommandParam(arrayStart = true) String[] moveArray, String moveTo) {
        List<String> moved = new ArrayList<>();
        for (String move : moveArray) {
            moved.addAll(MojangAPI.getEarlierNames(move));
        }

        move(moved, MojangAPI.getEarlierNames(moveTo));
        return true;
    }
}
