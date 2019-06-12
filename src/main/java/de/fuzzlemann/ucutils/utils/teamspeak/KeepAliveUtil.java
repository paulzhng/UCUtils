package de.fuzzlemann.ucutils.utils.teamspeak;

import de.fuzzlemann.ucutils.utils.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
public class KeepAliveUtil {

    private static boolean started;

    public static void start() {
        if (started) return;
        started = true;

        new Thread(() -> {
            while (true) {
                TSClientQuery.rawExec("whoami", false);

                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(1));
                } catch (InterruptedException e) {
                    Logger.LOGGER.catching(e);
                    Thread.currentThread().interrupt();
                }
            }
        }, "UCUtils-KeepAliveTimer").start();
    }
}
