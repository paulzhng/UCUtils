package de.fuzzlemann.ucutils.teamspeak;

import com.google.common.util.concurrent.Uninterruptibles;
import de.fuzzlemann.ucutils.commands.faction.ChannelActivityCommand;
import de.fuzzlemann.ucutils.commands.teamspeak.MoveHereCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.*;
import de.fuzzlemann.ucutils.teamspeak.objects.Channel;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.utils.abstraction.TestPlayer;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
@Ignore
public class TSClientQueryTest {

    @BeforeAll
    static void setUp() throws IOException {
        new TSAPIKeyLoader().load();
        AbstractionLayer.getInstance().setPlayerImplementation(TestPlayer.class);

        TSClientQuery.getInstance();
    }

    @AfterAll
    static void tearDown() {
        TSClientQuery.disconnect();
    }

    @Test
    void whoAmITest() {
        new WhoAmICommand().execute();
    }

    @Test
    void clientListTest() {
        new ClientListCommand().execute();
    }

    @Test
    void clientVariableTest() {
        new ClientVariableCommand(296).execute();
    }

    @Test
    void channelClientList() {
        Logger.LOGGER.info(new ChannelActivityCommand().getPlayersInChannel());
    }

    @Test
    void moveHereCommand()  {
        MoveHereCommand moveHereCommand = new MoveHereCommand();
        moveHereCommand.onCommand(new String[]{"dommenike"});
    }

    @Test
    void test() {
        ChannelClientListCommand.Response channelClientListCommandResponse  = new ChannelClientListCommand(66).getResponse();
        List<Client> clients = channelClientListCommandResponse.getClients();

        int[] channelIDs = {103, 86, 134, 87, 88, 156, 89, 553, 555, 90};

        for (int channelID : channelIDs) {
            new ClientMoveCommand(channelID, clients).execute();
            Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
        }

        for (int i = channelIDs.length - 1; i >= 0; i--) {
            new ClientMoveCommand(channelIDs[i], clients).execute();
            Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
        }

        Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    void test2() {
        ChannelListCommand.Response response = new ChannelListCommand().getResponse();
        for (Channel channel : response.getChannels()) {
            Logger.LOGGER.info(channel.getName());
        }
    }
}