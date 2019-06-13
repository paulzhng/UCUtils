package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.teamspeak.commands.AuthCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.BaseCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientNotifyRegisterCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.CurrentSchandlerIDCommand;
import de.fuzzlemann.ucutils.teamspeak.exceptions.ClientQueryAuthenticationException;
import de.fuzzlemann.ucutils.teamspeak.exceptions.ClientQueryConnectionException;
import de.fuzzlemann.ucutils.utils.Logger;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Fuzzlemann
 */
public class TSClientQuery implements Closeable {

    private static TSClientQuery instance;

    public static TSClientQuery getInstance() {
        if (instance == null) {
            instance = new TSClientQuery(UCUtilsConfig.tsAPIKey);
            try {
                instance.connect();
            } catch (IOException e) {
                throw new ClientQueryConnectionException("TeamSpeak ClientQuery failed setting up a connection", e);
            }
        }

        return instance;
    }

    public static void reconnect() {
        disconnect();
        getInstance();
    }

    public static void disconnect() {
        if (instance != null) {
            Logger.LOGGER.info("Closing the TeamSpeak Client Query Connection...");
            instance.close();
        }
    }

    private final String apiKey;
    private Socket socket;
    private volatile ClientQueryWriter writer;
    private volatile ClientQueryReader reader;
    private volatile KeepAliveThread keepAliveThread;
    private volatile boolean authenticated;
    private volatile int schandlerID;

    private TSClientQuery(String apiKey) {
        this.apiKey = apiKey;
    }

    public void executeCommand(BaseCommand<?> command) {
        try {
            writer.getQueue().put(command);
        } catch (InterruptedException e) {
            Logger.LOGGER.catching(e);
            Thread.currentThread().interrupt();
        }
    }

    private void connect() throws IOException {
        Logger.LOGGER.info("Setting up the TeamSpeak Client Query Connection...");

        setupConnection();
        authenticate();
        setupSchandlerID();
        registerEvents();
    }

    private void setupConnection() throws IOException {
        socket = new Socket("127.0.0.1", 25639);

        socket.setTcpNoDelay(true);
        socket.setSoTimeout(4000);

        writer = new ClientQueryWriter(this, new PrintWriter(socket.getOutputStream(), true));
        reader = new ClientQueryReader(this, new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)));

        //welcome messages
        while (reader.getReader().ready()) {
            reader.getReader().readLine();
        }

        writer.start();
        reader.start();

        keepAliveThread = new KeepAliveThread(this);
        keepAliveThread.start();
    }

    private void authenticate() {
        if (apiKey.length() != 29)
            throw new ClientQueryAuthenticationException("API Key was not entered correctly (apiKey.length() != 29)");

        AuthCommand authCommand = new AuthCommand(apiKey);
        authCommand.execute(this);

        CommandResponse response = authCommand.getResponse();
        if (!response.succeeded()) throw new ClientQueryAuthenticationException("API Key was not entered correctly");

        authenticated = true;
    }

    private void setupSchandlerID() {
        CurrentSchandlerIDCommand.Response response = new CurrentSchandlerIDCommand().getResponse();
        this.schandlerID = response.getSchandlerID();
    }

    private void registerEvents() {
        int schandlerID = TSClientQuery.getInstance().getSchandlerID();
        for (String eventName : TSEventHandler.TEAMSPEAK_EVENTS.keySet()) {
            new ClientNotifyRegisterCommand(schandlerID, eventName).execute();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientQueryWriter getWriter() {
        return writer;
    }

    public ClientQueryReader getReader() {
        return reader;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public int getSchandlerID() {
        return schandlerID;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(socket, writer, reader, keepAliveThread);
        TSClientQuery.instance = null;
    }
}
