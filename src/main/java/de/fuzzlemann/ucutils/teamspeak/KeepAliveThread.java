package de.fuzzlemann.ucutils.teamspeak;

import com.google.common.util.concurrent.Uninterruptibles;
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

            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void close() {
        closed = true;
    }
}
