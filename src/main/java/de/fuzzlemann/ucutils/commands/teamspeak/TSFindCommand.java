package de.fuzzlemann.ucutils.commands.teamspeak;

import de.fuzzlemann.ucutils.teamspeak.TSUtils;
import de.fuzzlemann.ucutils.teamspeak.commands.ChannelListCommand;
import de.fuzzlemann.ucutils.teamspeak.objects.Channel;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class TSFindCommand {

    @Command(value = "tsfind", usage = "/%label% [Spieler]", async = true)
    public boolean onCommand(String name) {
        List<String> names = MojangAPI.getEarlierNames(name);
        List<Client> clients = TSUtils.getClientsByName(names);
        if (clients.isEmpty()) {
            TextUtils.error("Es wurde kein Spieler auf dem TeamSpeak mit diesem Namen gefunden.");
            return true;
        }

        Client client = clients.get(0);

        ChannelListCommand.Response channelListResponse = new ChannelListCommand().getResponse();
        Channel channel = channelListResponse.getChannels().stream().filter(channelIter -> client.getChannelID() == channelIter.getChannelID()).findFirst().orElse(null);

        if (channel == null) throw new IllegalStateException();

        Message.builder()
                .prefix()
                .of(name).color(TextFormatting.BLUE).advance()
                .of(" befindet sich im Channel \"").color(TextFormatting.GRAY).advance()
                .of(channel.getName()).color(TextFormatting.BLUE).advance()
                .of("\".").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }

}
