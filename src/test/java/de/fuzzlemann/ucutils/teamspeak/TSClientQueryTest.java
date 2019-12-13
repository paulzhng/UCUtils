package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.commands.faction.ChannelActivityCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientListCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientVariableCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.WhoAmICommand;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.TestPlayer;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
}