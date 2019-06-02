package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MoveHereCommand implements CommandExecutor {

    @Override
    @Command(labels = "movehere", usage = "/%label% [Spieler...]", async = true)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        List<String> names = new ArrayList<>();
        for (String arg : args) {
            names.addAll(MojangAPI.getEarlierNames(arg));
        }

        moveHere(names);
        return true;
    }

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
        TextUtils.simplePrefixMessage("Du hast die Personen zu dir gemoved.");
    }
}
