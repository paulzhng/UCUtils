package de.fuzzlemann.ucutils.utils.chatlog;

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
                String message = queue.poll();
                if (message == null) continue;

                this.chatLogger.getLogger().info(message);
            }
        }).start();
    }

    void log(String message) {
        queue.offer(message);
    }
}
