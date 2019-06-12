package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.teamspeak.commands.BaseCommand;
import org.apache.commons.io.IOUtils;

import java.io.Closeable;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Fuzzlemann
 */
public class ClientQueryWriter extends Thread implements Closeable {

    private final BlockingQueue<BaseCommand> queue = new LinkedBlockingQueue<>();
    private final TSClientQuery query;
    private final PrintWriter writer;
    private volatile boolean closed;

    ClientQueryWriter(TSClientQuery query, PrintWriter writer) {
        this.query = query;
        this.writer = writer;

        setName("UCUtils-TSClientQuery-ClientQueryWriter");
    }

    @Override
    public void run() {
        while (!closed) {
            BaseCommand command;
            while ((command = queue.poll()) != null) {
                try {
                    query.getReader().getQueue().put(command);
                } catch (InterruptedException e) {
                    Logger.LOGGER.catching(e);
                    Thread.currentThread().interrupt();
                }

                Logger.LOGGER.info("CLIENT QUERY WRITER: " + command.getCommand());
                writer.println(command.getCommand());
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                Logger.LOGGER.catching(e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void close() {
        closed = true;
        IOUtils.closeQuietly(writer);
    }

    public BlockingQueue<BaseCommand> getQueue() {
        return queue;
    }

    public TSClientQuery getQuery() {
        return query;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public boolean isClosed() {
        return closed;
    }
}
