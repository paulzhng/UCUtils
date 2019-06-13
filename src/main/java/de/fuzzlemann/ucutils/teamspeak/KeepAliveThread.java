package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.teamspeak.commands.WhoAmICommand;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
public class KeepAliveThread extends Thread implements Closeable {

    private final TSClientQuery clientQuery;

    public KeepAliveThread(TSClientQuery clientQuery) {
        this.clientQuery = clientQuery;

        setName("UCUtils-TSClientQuery-KeepAliveThread");
    }

    private volatile boolean closed;

    @Override
    public void run() {
        while (!closed) {
            if (!clientQuery.isAuthenticated()) continue;

            new WhoAmICommand().execute(clientQuery);

            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(1));
            } catch (InterruptedException e) {
                Logger.LOGGER.catching(e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void close() {
        closed = true;
    }
}
