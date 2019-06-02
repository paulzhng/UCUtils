package de.fuzzlemann.ucutils.utils.chatlog;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.config.ConfigUtil;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class ChatLogger {

    public static ChatLogger instance;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private final ChatLogConsumer consumer;
    private final Logger logger;

    public ChatLogger() {
        File directory = new File(Main.MINECRAFT.mcDataDir, "chatlogs");

        deleteTempFiles(directory);
        FileHandler fileHandler = createChatLog(directory);
        if (fileHandler == null) {
            logger = null;
            consumer = null;
            return;
        }

        logger = Logger.getLogger("ChatLogger");
        logger.addHandler(fileHandler);

        consumer = new ChatLogConsumer(this);

        log("------------ Chat Log ------------");
    }

    Logger getLogger() {
        return logger;
    }

    private void deleteTempFiles(File directory) {
        FilenameFilter filenameFilter = (dir, name) -> name.endsWith(".txt.lck");
        File[] files = directory.listFiles(filenameFilter);
        if (files == null) return;

        for (File file : files) {
            if (!file.delete())
                de.fuzzlemann.ucutils.utils.Logger.LOGGER.info("Temporary file " + file.getPath() + " couldn't be deleted!");
        }
    }

    private FileHandler createChatLog(File directory) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss").format(new Date());

        FileHandler fileHandler;
        try {
            File logFile = new File(directory, "chatlog-" + timeStamp + ".txt");
            if (directory.mkdirs())
                de.fuzzlemann.ucutils.utils.Logger.LOGGER.info("ChatLog Directory created");

            String path = logFile.getPath();
            if (logFile.createNewFile())
                de.fuzzlemann.ucutils.utils.Logger.LOGGER.info("ChatLog " + path + " created");
            fileHandler = new FileHandler(path);
        } catch (IOException e) {
            ConfigUtil.logChat = false;
            de.fuzzlemann.ucutils.utils.Logger.LOGGER.catching(e);
            return null;
        }

        fileHandler.setFormatter(new ChatLogFormatter());

        return fileHandler;
    }

    private void log(String message) {
        if (!ConfigUtil.logChat || consumer == null) return;

        consumer.log(message + "\n");
    }

    @SubscribeEvent
    public static void onChat(ClientChatEvent e) {
        if (instance == null) return;

        instance.log("[SELF] " + e.getOriginalMessage());
    }

    @SubscribeEvent
    public static void onReceiveChat(ClientChatReceivedEvent e) {
        if (instance == null) return;

        instance.log("[CHAT] " + e.getMessage().getUnformattedText());
    }

    @SubscribeEvent
    public static void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        if (instance == null) return;

        NetworkManager manager = e.getManager();
        String address = manager.isLocalChannel() ? "local world" : manager.getRemoteAddress().toString();

        instance.log("---- Connected to " + address + " ----");
    }

    @SubscribeEvent
    public static void onClientConnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        if (instance == null) return;

        NetworkManager manager = e.getManager();
        String address = manager.isLocalChannel() ? "local world" : manager.getRemoteAddress().toString();

        instance.log("---- Disconnected from " + address + " ----");
    }

    private class ChatLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return dateFormat.format(new Date()) + " | " + record.getMessage();
        }
    }
}
