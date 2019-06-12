package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.teamspeak.commands.BaseCommand;
import de.fuzzlemann.ucutils.teamspeak.events.TSEvent;
import de.fuzzlemann.ucutils.utils.Logger;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Fuzzlemann
 */
public class ClientQueryReader extends Thread implements Closeable {

    private final BlockingQueue<BaseCommand> queue = new LinkedBlockingQueue<>();
    private final TSClientQuery query;
    private final BufferedReader reader;
    private volatile boolean closed;

    ClientQueryReader(TSClientQuery query, BufferedReader reader) {
        this.query = query;
        this.reader = reader;

        setName("UCUtils-TSClientQuery-ClientQueryReader");
    }

    @Override
    public void run() {
        while (!closed) {
            try {
                while (reader.ready()) {
                    String line = reader.readLine();
                    if (line.isEmpty()) continue;

                    if (line.startsWith("notify")) {
                        TSEvent event = TSEventHandler.getEvent(line);
                        if (event != null) {
                            MinecraftForge.EVENT_BUS.post(event);
                            continue;
                        }
                    }

                    BaseCommand<?> baseCommand = queue.poll();
                    if (baseCommand != null) {
                        List<String> lines = new ArrayList<>();
                        lines.add(line.trim());

                        if (!line.startsWith("error")) {
                            String statusLine;
                            while ((statusLine = reader.readLine()) != null) {
                                statusLine = statusLine.trim();
                                if (statusLine.isEmpty()) continue;

                                lines.add(statusLine);
                                break;
                            }
                        }

                        line = String.join(" ", lines);

                        Logger.LOGGER.info("CLIENT QUERY READER: " + line);

                        ParameterizedType genericSuperclass = (ParameterizedType) baseCommand.getClass().getGenericSuperclass();
                        String className = genericSuperclass.getActualTypeArguments()[0].getTypeName();
                        Class<?> responseClass = Class.forName(className);

                        Object commandResponse = responseClass.getConstructor(String.class).newInstance(line);

                        baseCommand.getResponseFuture().complete(commandResponse);
                    }
                }
            } catch (IOException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                Logger.LOGGER.catching(e);
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
        IOUtils.closeQuietly(reader);
    }

    public BlockingQueue<BaseCommand> getQueue() {
        return queue;
    }

    public TSClientQuery getQuery() {
        return query;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public boolean isClosed() {
        return closed;
    }
}
