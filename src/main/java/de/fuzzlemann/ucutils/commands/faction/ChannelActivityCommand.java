package de.fuzzlemann.ucutils.commands.faction;

import com.google.common.collect.Multimap;
import de.fuzzlemann.ucutils.events.MemberActivityEventHandler;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.teamspeak.ResultParser;
import de.fuzzlemann.ucutils.utils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ChannelActivityCommand {

    @Command(value = "channelactivity", async = true)
    public boolean onCommand(UPlayer p, @CommandParam(required = false, requiredValue = "copy") boolean copy) {
        List<String> players;
        try {
            players = getPlayersInChannel(true);
        } catch (NullPointerException e) {
            Logger.LOGGER.catching(e);
            TextUtils.error("Du hast dein API Key noch nicht oder falsch gesetzt.");
            return true;
        }

        if (players.isEmpty()) {
            TextUtils.error("Du bist nicht im TeamSpeak online.");
            return true;
        }

        if (!players.contains(p.getName())) players.add(p.getName());

        List<String> members = new ArrayList<>(MemberActivityEventHandler.MEMBER_LIST);

        if (members.isEmpty()) {
            TextUtils.error("Du hast /memberactivity noch nicht ausgeführt.");
            return true;
        }

        members.removeAll(players);
        removeEarlierNames(players, members);

        if (copy) {
            copyList(members);
            TextUtils.simpleMessage("Du hast die Liste von nicht-anwesenden Spielern kopiert.");
        } else {
            sendList(members);
        }

        return true;
    }

    private void sendList(List<String> members) {
        Message.MessageBuilder builder = Message.builder();

        builder.of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Nicht anwesende Fraktionsmitglieder\n").color(TextFormatting.DARK_AQUA).advance();
        for (String member : members) {
            builder.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                    .of(member + "\n").color(TextFormatting.GRAY).advance();
        }

        builder.of(" » ").color(TextFormatting.DARK_GRAY).advance()
                .of("⟳")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Aktualisieren", TextFormatting.DARK_AQUA))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/channelactivity")
                .color(TextFormatting.DARK_PURPLE).advance()
                .space()
                .of("⎘")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Kopieren", TextFormatting.DARK_AQUA))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/channelactivity copy")
                .color(TextFormatting.DARK_PURPLE).advance();

        builder.send();
    }

    private void copyList(List<String> members) {
        StringBuilder sb = new StringBuilder("[color=#008080][b]Nicht-Anwesende Member:[/b][/color]");

        for (String member : members) {
            sb.append("\n- ").append(member);
        }

        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void removeEarlierNames(List<String> players, List<String> members) {
        Multimap<String, String> earlierNames = MojangAPI.getEarlierNames(members);
        for (String name : earlierNames.values()) {
            for (String earlierName : earlierNames.get(name)) {
                if (players.stream().anyMatch(player -> player.equalsIgnoreCase(earlierName))) {
                    members.remove(name);
                    break;
                }
            }
        }
    }

    private List<String> getPlayersInChannel(boolean retry) {
        Map<String, String> whoAmIResult = TSClientQuery.exec("whoami");
        String cid = whoAmIResult.get("cid");

        if (cid == null && retry) {
            TSClientQuery.connect();
            return getPlayersInChannel(false);
        }

        String channelClientListResult = TSClientQuery.rawExec("channelclientlist cid=" + cid, false);
        if (channelClientListResult == null) return Collections.emptyList();

        List<String> clientIDs = new ArrayList<>();
        for (String channelClient : StringUtils.split(channelClientListResult, "|")) {
            Map<String, String> channelClientOptions = ResultParser.parse(channelClient);

            clientIDs.add(channelClientOptions.get("clid"));
        }

        List<String> descriptions = new ArrayList<>();
        for (String clientID : clientIDs) {
            Map<String, String> clientVariableResult = TSClientQuery.exec("clientvariable client_description clid=" + clientID);

            String description = clientVariableResult.get("client_description");
            if (description != null)
                descriptions.add(TextUtils.stripPrefix(description));
        }

        return descriptions;
    }
}
