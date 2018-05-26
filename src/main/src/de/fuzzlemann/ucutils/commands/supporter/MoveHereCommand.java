package de.fuzzlemann.ucutils.commands.supporter;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.teamspeak.ResultParser;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
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
public class MoveHereCommand implements CommandExecutor {

    @Override
    @Command(labels = "movehere", usage = "/%label% [Spieler...]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        List<String> names = new ArrayList<>();
        for (String arg : args) {
            names.addAll(MojangAPI.getEarlierNames(arg));
        }

        if (names.isEmpty()) {
            TextUtils.error("Es wurde kein Spieler mit diesem Namen gefunden.");
            return true;
        }

        new Thread(() -> moveHere(names, true)).start();
        return true;
    }

    private void moveHere(List<String> names, boolean retry) {
        Map<String, String> whoAmIResult = TSClientQuery.exec("whoami");
        String channelID = whoAmIResult.get("cid");

        if (channelID == null && retry) {
            TSClientQuery.connect();
            moveHere(names, false);
            return;
        }

        String clientString = TSClientQuery.rawExec("clientlist", false);
        List<Map<String, String>> clientMap = ResultParser.parseMap(clientString);

        List<Integer> clientIDs = new ArrayList<>();

        for (Map<String, String> map : clientMap) {
            int clientID = Integer.parseInt(map.get("clid"));

            Map<String, String> result = TSClientQuery.exec("clientvariable clid=" + clientID + " client_description");
            String description = result.get("client_description");
            if (description == null) continue;
            if (!names.contains(TextUtils.stripPrefix(description))) continue;

            clientIDs.add(clientID);
        }

        if (clientIDs.isEmpty()) {
            TextUtils.error("Der Spieler ist nicht auf dem TeamSpeak.");
            return;
        }

        StringJoiner stringJoiner = new StringJoiner("|");
        for (int clientID : clientIDs) {
            stringJoiner.add("clid=" + clientID);
        }

        TSClientQuery.exec("clientmove cid=" + channelID + " " + stringJoiner);
        Main.MINECRAFT.player.sendMessage(TextUtils.simpleMessage("Die Aktion wurde erfolgreich ausgeführt.", TextFormatting.GREEN));
    }
}
