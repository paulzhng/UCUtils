package de.fuzzlemann.ucutils.utils.chatlog;

import de.fuzzlemann.ucutils.utils.config.ConfigUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Fuzzlemann
 */
class ChatLogConsumer {

    private final ChatLogger chatLogger;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    ChatLogConsumer(ChatLogger chatLogger) {
        this.chatLogger = chatLogger;
        new Thread(() -> {
            while (true) {
                if (!ConfigUtil.logChat) continue;

                String message;
                while ((message = queue.poll()) != null) {
                    this.chatLogger.getLogger().info(message);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }, "UCUtils-ChatLogConsumer").start();
    }

    void log(String message) {
        queue.offer(message);
    }
}
