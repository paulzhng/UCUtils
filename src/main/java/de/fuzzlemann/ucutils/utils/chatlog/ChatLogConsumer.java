package de.fuzzlemann.ucutils.utils.chatlog;

import com.google.common.util.concurrent.Uninterruptibles;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
                if (!UCUtilsConfig.logChat) continue;

                String message;
                while ((message = queue.poll()) != null) {
                    this.chatLogger.getLogger().info(message);
                }

                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            }
        }, "UCUtils-ChatLogConsumer").start();
    }

    void log(String message) {
        queue.offer(message);
    }
}
