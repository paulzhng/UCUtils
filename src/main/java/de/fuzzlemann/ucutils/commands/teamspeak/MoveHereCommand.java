package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientMoveCommand;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MoveHereCommand {

    @Command(value = "movehere", usage = "/%label% [Spieler...]", async = true)
    public boolean onCommand(String[] players) {
        if (players.length == 0) return false;

        List<String> names = new ArrayList<>();
        for (String arg : players) {
            names.addAll(MojangAPI.getEarlierNames(arg));
        }

        int channelID = TSUtils.getMyChannelID();
        List<Client> clients = TSUtils.getClientsByName(names);
        if (clients.isEmpty()) {
            TextUtils.error("Es wurde kein Spieler auf dem TeamSpeak mit diesem Namen gefunden.");
            return true;
        }

        CommandResponse response = new ClientMoveCommand(channelID, clients).getResponse();
        if (!response.succeeded()) {
            TextUtils.error("Das Moven ist fehlgeschlagen.");
            return true;
        }

        TextUtils.simpleMessage("Du hast die Personen zu dir gemoved.");
        return true;
    }
}
