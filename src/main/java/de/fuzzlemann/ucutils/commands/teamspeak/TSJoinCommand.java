package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.teamspeak.commands.ChannelListCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientMoveCommand;
import de.fuzzlemann.ucutils.teamspeak.objects.Channel;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

/**
 * @author Fuzzlemann
 */
public class TSJoinCommand implements TabCompletion {

    @Command(value = "tsjoin", usage = "/%label% [Channel] (Passwort)", async = true)
    public boolean onCommand(String channelName,
                             @CommandParam(joinStart = true, required = false, defaultValue = CommandParam.NULL) String password) {
        ChannelListCommand.Response channelListResponse = new ChannelListCommand().getResponse();
        if (!channelListResponse.succeeded()) {
            TextUtils.error("Das Bewegen ist fehlgeschlagen.");
            return true;
        }

        Map<String, Channel> channelMaps = new HashMap<>();
        for (Channel channel : channelListResponse.getChannels()) {
            String name = channel.getName();
            if (name.startsWith("[cspacer")) continue;
            if (name.startsWith("[spacer")) continue;

            name = modifyChannelName(name);

            channelMaps.put(name, channel);
        }

        channelName = channelName.replace('-', ' ');
        Channel foundChannel;
        if (channelName.equalsIgnoreCase("Öffentlich") && Faction.getFactionOfPlayer() != null) {
            foundChannel = new Channel(Faction.getFactionOfPlayer().getPublicChannelID(), "Öffentlich", 0, 0);
        } else {
            foundChannel = ForgeUtils.getMostMatching(channelMaps.values(), channelName, (channel) -> modifyChannelName(channel.getName()));
        }

        if (foundChannel == null) {
            TextUtils.error("Es wurde kein Channel gefunden.");
            return true;
        }

        ClientMoveCommand clientMoveCommand;
        if (password == null) {
            clientMoveCommand = new ClientMoveCommand(foundChannel.getChannelID(), TSUtils.getMyClientID());
        } else {
            clientMoveCommand = new ClientMoveCommand(foundChannel.getChannelID(), password);
        }

        CommandResponse commandResponse = clientMoveCommand.getResponse();
        if (!commandResponse.succeeded()) {
            TextUtils.error("Das Bewegen ist fehlgeschlagen.");
            return true;
        }

        Message.builder()
                .prefix()
                .of("Du hast dich zu dem Channel \"").color(TextFormatting.GRAY).advance()
                .of(foundChannel.getName()).color(TextFormatting.BLUE).advance()
                .of("\" bewegt.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        ChannelListCommand.Response response = new ChannelListCommand().getResponse();
        Set<String> names = new HashSet<>(); // use hashset to dedup
        for (Channel channel : response.getChannels()) {
            String name = channel.getName();
            if (name.startsWith("[cspacer")) continue;
            if (name.startsWith("[spacer")) continue;

            name = modifyChannelName(name);
            names.add(name);
        }

        return new ArrayList<>(names);
    }

    private String modifyChannelName(String input) {
        input = input.replace("»", "");
        input = input.trim();

        return input;
    }
}
