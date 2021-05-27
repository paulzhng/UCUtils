package de.fuzzlemann.ucutils.commands.jobs;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ADropTransportCommand {

    private final Timer timer = new Timer();
    private final AtomicBoolean started = new AtomicBoolean();

    @Command("adroptransport")
    public boolean onCommand(UPlayer p) {
        if (started.get()) return true;

        Scoreboard scoreboard = p.getWorldScoreboard();

        Optional<Score> score = scoreboard.getScores().stream()
                .filter(scorePredicate -> {
                    String playerName = scorePredicate.getPlayerName();

                    return playerName.equals("§aTransport") || playerName.equals("§9Kisten§8: ") || playerName.equals("§9Waffenkisten§8: ") || playerName.equals("§6Weizen§8:") || playerName.equals("§9Schwarzpulver§8:");
                })
                .findFirst();

        if (!score.isPresent()) {
            TextUtils.error("Du bist derzeit in keinem Transport.");
            return true;
        }

        int amount = score.get().getScorePoints();

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
        }, 0L, TimeUnit.SECONDS.toMillis(10));
        return true;
    }
}
