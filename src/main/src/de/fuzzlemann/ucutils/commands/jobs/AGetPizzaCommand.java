package de.fuzzlemann.ucutils.commands.jobs;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
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
public class AGetPizzaCommand implements CommandExecutor {

    private final Timer TIMER = new Timer();
    private final AtomicBoolean STARTED = new AtomicBoolean();

    @Override
    @Command(labels = "agetpizza")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (STARTED.get()) return true;

        Scoreboard scoreboard = p.getWorldScoreboard();

        Score score = scoreboard.getScores().stream()
                .filter(scorePredicate -> scorePredicate.getPlayerName().equals("\u00a72Pizzen\u00a78: "))
                .findFirst()
                .orElse(null);

        if (score == null || score.getScorePoints() != 0) {
            TextUtils.error("Du musst derzeit keine Pizzen einpacken.", p);
            return true;
        }

        STARTED.set(true);

        TIMER.scheduleAtFixedRate(new TimerTask() {
            private int i;

            @Override
            public void run() {
                p.sendChatMessage("/getpizza");
                if (i++ > 15) {
                    STARTED.set(false);
                    cancel();
                }
            }
        }, 0L, TimeUnit.SECONDS.toMillis((long) 3.1));
        return true;
    }
}