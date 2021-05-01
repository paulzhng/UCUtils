package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.commands.faction.AFBankEinzahlen;
import de.fuzzlemann.ucutils.commands.time.ClockCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Dimikou
 */
public class ADropAmmo implements TabCompletion{

    private final Timer timer = new Timer();
    private int ammo;

    @Command(value = "adropammo", usage = "/%label% [Waffe] [Munition]")
    public boolean onCommand(UPlayer p, String weapon, int ammo) {
        if (!weapon.equalsIgnoreCase("M4") && !weapon.equalsIgnoreCase("MP5") && !weapon.equalsIgnoreCase("Jagdlinte")) return false;

        if (!(ammo > 0)) return false;

        ADropAmmo instance = this;
        instance.ammo = ammo;

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (instance.ammo > 100) {
                    p.sendChatMessage("/dropammo " + weapon + " 100");
                    instance.ammo -= 100;
                } else {
                    p.sendChatMessage("/dropammo " + weapon + " " + instance.ammo);
                    cancel();
                }
            }
        }, 0, TimeUnit.SECONDS.toMillis(1));

        return true;
    }

    public List<String> getTabCompletions(UPlayer p, String[] args) {
        return Arrays.asList("M4", "MP5", "Jagdflinte");
    }

}
