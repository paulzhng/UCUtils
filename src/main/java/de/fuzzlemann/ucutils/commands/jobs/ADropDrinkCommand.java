package de.fuzzlemann.ucutils.commands.jobs;

import de.fuzzlemann.ucutils.utils.command.api.Command;
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
public class ADropDrinkCommand {

    private final Timer timer = new Timer();
    private final AtomicBoolean started = new AtomicBoolean();

    @Command("adropdrink")
    public boolean onCommand(EntityPlayerSP p) {
        if (started.get()) return true;

        Scoreboard scoreboard = p.getWorldScoreboard();
        Score score = scoreboard.getScores().stream()
                .filter(scorePredicate -> scorePredicate.getPlayerName().equals("§9Getränke§8: "))
                .findFirst()
                .orElse(null);

        if (score == null) {
            TextUtils.error("Du lieferst derzeit keine Getränke aus.");
            return true;
        }

        int amount = score.getScorePoints();

        started.set(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            private int i;

            @Override
            public void run() {
                p.sendChatMessage("/dropdrink");
                if (i++ > amount) {
                    started.set(false);
                    cancel();
                }
            }
        }, 0L, TimeUnit.SECONDS.toMillis((long) 2.1));
        return true;
    }
}