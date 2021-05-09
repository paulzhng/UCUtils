package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Dimiikou
 */
@Mod.EventBusSubscriber
public class ADropAmmo implements TabCompletion {

    private static final AtomicBoolean STARTED = new AtomicBoolean();
    private final List<String> weapons = Arrays.asList("m4", "mp5", "jagdflinte", "pistole");

    @Command(value = "adropammo", usage = "/%label% [Waffe] [Munition]")
    public boolean onCommand(UPlayer p, String weapon, int ammunition) {
        if (!weapons.contains(weapon.toLowerCase())) return false;
        if (ammunition <= 0) return false;

        if (STARTED.get()) {
            TextUtils.error("Der Befehl wird derzeit bereits ausgeführt.");
            return true;
        }

        STARTED.set(true);

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            private int remainingAmmunition = ammunition;

            public void run() {
                if (!STARTED.get()) {
                    cancel();
                    return;
                }

                if (remainingAmmunition > 100) {
                    p.sendChatMessage("/dropammo " + weapon + " 100");
                    remainingAmmunition -= 100;
                } else {
                    p.sendChatMessage("/dropammo " + weapon + " " + remainingAmmunition);
                    cancel();
                    STARTED.set(false);
                }
            }
        }, 0, TimeUnit.SECONDS.toMillis(1));

        return true;
    }

    @SubscribeEvent
    public static void onDropFeedback(ClientChatReceivedEvent e) {
        if (!STARTED.get()) return;

        String msg = e.getMessage().getUnformattedText();
        if (msg.equals("Fehler: Du bist nicht in deinem Haus.") || msg.equals("Fehler: Du hast nicht genug Munition in der Waffe.")) {
            STARTED.set(false);
        }
    }

    public List<String> getTabCompletions(UPlayer p, String[] args) {
        return weapons;
    }
}
