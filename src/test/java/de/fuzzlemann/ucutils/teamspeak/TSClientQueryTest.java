package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.commands.teamspeak.MoveHereCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ChannelClientListCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientListCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientVariableCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.WhoAmICommand;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionHandler;
import de.fuzzlemann.ucutils.utils.abstraction.TestPlayer;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@Ignore
public class TSClientQueryTest {

    @BeforeAll
    static void setUp() throws IOException {
        new TSAPIKeyLoader().load();
        AbstractionHandler.getInstance().setPlayerImplementation(TestPlayer.class);

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
        ChannelClientListCommand.Response response = new ChannelClientListCommand(156).execute().getResponse();
        List<Client> clients = response.getClients();
        for (Client client : clients) {
            Logger.LOGGER.info("name: " + client.getName());
        }
    }

    @Test
    void moveHereCommand()  {
        MoveHereCommand moveHereCommand = new MoveHereCommand();
        moveHereCommand.onCommand(new String[]{"dommenike"});
    }
}