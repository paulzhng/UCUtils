package de.fuzzlemann.ucutils.commands.jobs;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class AGetPizzaCommand {

    private final Timer timer = new Timer();
    private final AtomicBoolean started = new AtomicBoolean();

    @Command("agetpizza")
    public boolean onCommand(UPlayer p) {
        if (started.get()) return true;

        Scoreboard scoreboard = p.getWorldScoreboard();
        Score score = scoreboard.getScores().stream()
                .filter(scorePredicate -> scorePredicate.getPlayerName().equals("§2Pizzen§8: "))
                .findFirst()
                .orElse(null);

        if (score == null || score.getScorePoints() != 0) {
            TextUtils.error("Du musst derzeit keine Pizzen einpacken.");
            return true;
        }

        started.set(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            private int i;

            @Override
            public void run() {
                p.sendChatMessage("/getpizza");
                if (i++ > 15) {
                    started.set(false);
                    cancel();
                }
            }
        }, 0L, TimeUnit.SECONDS.toMillis((long) 3.1));
        return true;
    }
}