package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MoveHereCommand {

    private static void moveHere(List<String> names) {
        Optional<Integer> myChannelIDOptional = TSUtils.getMyChannelID();
        if (!myChannelIDOptional.isPresent()) return;

        List<Map<String, String>> clients = TSUtils.getClientsByName(names);
        if (clients.isEmpty()) {
            TextUtils.error("Es wurde kein Spieler auf dem TeamSpeak mit diesem Namen gefunden.");
            return;
        }

        StringJoiner stringJoiner = new StringJoiner("|");
        for (Map<String, String> client : clients) {
            stringJoiner.add("clid=" + client.get("clid"));
        }

        TSClientQuery.exec("clientmove cid=" + myChannelIDOptional.get() + " " + stringJoiner);
        TextUtils.simpleMessage("Du hast die Personen zu dir gemoved.");
    }

    @Command(value = "movehere", usage = "/%label% [Spieler...]", async = true)
    public boolean onCommand(String[] players) {
        if (players.length == 0) return false;

        List<String> names = new ArrayList<>();
        for (String arg : players) {
            names.addAll(MojangAPI.getEarlierNames(arg));
        }

        moveHere(names);
        return true;
    }
}
