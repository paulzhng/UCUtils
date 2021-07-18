package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.TabCompletion;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Dimiikou
 */
public class AGiveRank implements TabCompletion {

    private final Timer timer = new Timer();

    @Command(value = {"agiverank"}, usage = "/%label% [Rang] [Spieler...]")
    public boolean onCommand(UPlayer p, int rank, @CommandParam(arrayStart = true) String[] targets) {
        timer.scheduleAtFixedRate(new TimerTask() {
            private int i;

            @Override
            public void run() {
                if (i >= targets.length) {
                    cancel();
                    return;
                }

                String player = targets[i++];

                p.sendChatMessage("/giverank " + rank + " " + player);
            }
        }, 0, TimeUnit.SECONDS.toMillis(1));

        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        return Collections.emptyList();
    }
}
