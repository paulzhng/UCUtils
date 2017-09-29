package de.fuzzlemann.ucutils.commands.jobs;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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

    private Timer timer = new Timer();
    private AtomicBoolean started = new AtomicBoolean();

    @Override
    @Command(labels = "adroptransport")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (started.get()) return true;

        Scoreboard scoreboard = p.getWorldScoreboard();

        Score score = scoreboard.getScores().stream()
                .peek(scorePredicate -> System.out.println(scorePredicate.getPlayerName()))
                .filter(scorePredicate -> scorePredicate.getPlayerName().equals("\u00a79Kisten\u00a78: "))
                .findFirst()
                .orElse(null);

        if (score == null) {
            TextComponentString text = new TextComponentString("Du bist in keinem Transport!");
            text.getStyle().setColor(TextFormatting.RED);

            p.sendMessage(text);
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