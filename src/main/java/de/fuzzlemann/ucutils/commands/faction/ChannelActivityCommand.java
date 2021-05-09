package de.fuzzlemann.ucutils.commands.faction;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multimap;
import de.fuzzlemann.ucutils.events.MemberActivityEventHandler;
import de.fuzzlemann.ucutils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.teamspeak.commands.ChannelClientListCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientVariableCommand;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.*;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ChannelActivityCommand {

    @Command(value = "channelactivity", async = true)
    public boolean onCommand(UPlayer p, @CommandParam(required = false, requiredValue = "copy") boolean copy) {
        List<String> players = getPlayersInChannel();
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
            TextUtils.simpleMessage("Du hast die Liste mit den nicht-anwesenden Spielern kopiert.");
        } else {
            sendList(members);
        }

        return true;
    }

    private void sendList(List<String> members) {
        Message.builder()
                .of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Nicht anwesende Fraktionsmitglieder").color(TextFormatting.DARK_AQUA).advance()
                .newLine()
                .joiner(members)
                .consumer((b, member) -> b.of(" * ").color(TextFormatting.DARK_GRAY).advance()
                        .of(member).color(TextFormatting.GRAY).advance())
                .newLineJoiner()
                .advance()
                .newLine()
                .of(" » ").color(TextFormatting.DARK_GRAY).advance()
                .of("⟳")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Aktualisieren", TextFormatting.DARK_AQUA))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/channelactivity")
                .color(TextFormatting.DARK_PURPLE).advance()
                .space()
                .of("⎘")
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Kopieren", TextFormatting.DARK_AQUA))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, "/channelactivity copy")
                .color(TextFormatting.DARK_PURPLE).advance()
                .send();
    }

    private void copyList(List<String> members) {
        StringJoiner stringJoiner = new StringJoiner("\n");

        stringJoiner.add('<p><span style="color: CadetBlue"><strong>Nicht-Anwesende Member:</strong></span></p>');

        for (String member : members) {
            stringJoiner.add(member);
        }

        StringSelection stringSelection = new StringSelection(stringJoiner.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void removeEarlierNames(List<String> players, List<String> members) {
        Multimap<String, String> earlierNames = MojangAPI.getEarlierNames(members);
        for (Map.Entry<String, String> entry : earlierNames.entries()) {
            String name = entry.getKey();
            String earlierName = entry.getValue();

            if (players.stream().anyMatch(player -> player.equalsIgnoreCase(earlierName))) {
                members.remove(name);
                break;
            }
        }
    }

    @VisibleForTesting
    public List<String> getPlayersInChannel() {
        ChannelClientListCommand.Response channelClientListCommandResponse = new ChannelClientListCommand(TSUtils.getMyChannelID()).getResponse();
        if (!channelClientListCommandResponse.succeeded()) return Collections.emptyList();

        List<Client> clients = channelClientListCommandResponse.getClients();
        List<String> descriptions = new ArrayList<>();
        for (Client client : clients) {
            ClientVariableCommand.Response clientVariableCommandResponse = new ClientVariableCommand(client).getResponse();
            String minecraftName = clientVariableCommandResponse.getMinecraftName();

            if (minecraftName == null) continue;
            descriptions.add(minecraftName);
        }

        return descriptions;
    }
}
