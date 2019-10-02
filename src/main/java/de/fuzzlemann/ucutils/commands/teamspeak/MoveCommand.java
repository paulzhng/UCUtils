package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientMoveCommand;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandParam;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MoveCommand {

    @Command(value = "move", usage = "/%label% [Spieler...] [Ziel]", async = true)
    public boolean onCommand(@CommandParam(arrayStart = true) String[] moveArray, String moveTo) {
        List<String> moved = new ArrayList<>();
        for (String move : moveArray) {
            moved.addAll(MojangAPI.getEarlierNames(move));
        }

        List<Client> clientsMoved = TSUtils.getClientsByName(moved);
        List<Client> clientsMoveTo = TSUtils.getClientsByName(MojangAPI.getEarlierNames(moveTo));

        if (clientsMoved.isEmpty() || clientsMoveTo.isEmpty()) {
            TextUtils.error("Einer der Spieler befindet sich nicht auf dem TeamSpeak.");
            return true;
        }

        Client moveToClient = clientsMoveTo.get(0);
        CommandResponse response = new ClientMoveCommand(moveToClient.getChannelID(), clientsMoved).getResponse();
        if (!response.succeeded()) {
            TextUtils.error("Das Moven ist fehlgeschlagen.");
            return true;
        }

        TextUtils.simpleMessage("Du hast die Personen gemoved.");
        return true;
    }
}
