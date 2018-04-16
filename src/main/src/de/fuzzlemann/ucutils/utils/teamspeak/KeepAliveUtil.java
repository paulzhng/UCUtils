package de.fuzzlemann.ucutils.utils.teamspeak;

import java.util.Timer;
import java.util.TimerTask;
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
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    TSClientQuery.rawExec("whoami", false);
                }
            }, TimeUnit.MINUTES.toMillis(5), TimeUnit.MINUTES.toMillis(5));
        }).start();
    }
}
