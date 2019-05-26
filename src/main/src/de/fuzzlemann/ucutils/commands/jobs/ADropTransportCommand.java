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
public class ADropTransportCommand implements CommandExecutor {

    private final Timer timer = new Timer();
    private final AtomicBoolean started = new AtomicBoolean();

    @Override
    @Command(labels = "adroptransport")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (started.get()) return true;

        Scoreboard scoreboard = p.getWorldScoreboard();

        Score score = scoreboard.getScores().stream()
                .filter(scorePredicate -> {
                    String playerName = scorePredicate.getPlayerName();

                    return playerName.equals("§9Kisten§8: ") || playerName.equals("§9Waffenkisten§8: ") || playerName.equals("§6Weizen&8: ");
                })
                .findFirst()
                .orElse(null);

        if (score == null) {
            TextUtils.error("Du bist in keinem Transport");
            return true;
        }

        int amount = score.getScorePoints();

        started.set(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            private int i;

            @Override
            public void run() {
                p.sendChatMessage("/droptransport");
                if (i++ > amount) {
                    started.set(false);
                    cancel();
                }
            }
        }, 0L, TimeUnit.SECONDS.toMillis((long) 10.1));
        return true;
    }
}