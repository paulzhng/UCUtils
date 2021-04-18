package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.commands.time.ClockCommand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dimikou
 */
public class AFBankEinzahlen implements TabCompletion {

    private static final Pattern FBANK_TAXES = Pattern.compile("^\\[F-Bank] (?:\\[UC])*([a-zA-Z0-9_]+) hat (\\d+)\\$ \\((-\\d)\\$\\) .+ Fraktionsbank ([a-zA-Z_]+)\\.$");
    private static final AtomicBoolean STARTED = new AtomicBoolean();

    private final Timer timer = new Timer();
    private int amount;

    @Command(value = "afbank", usage = "/%label% [einzahlen/auszahlen] [Betrag]")
    public boolean onCommand(UPlayer p, String interaction, int amount) {
        if (STARTED.get()) return true;

        if (!interaction.equalsIgnoreCase("einzahlen") && !interaction.equalsIgnoreCase("auszahlen")) return false;

        // check if there are taxes
        p.sendChatMessage("/fbank " + interaction + " 4");
        this.amount = amount - 4; // we already paid 4$

        STARTED.set(true);

        AFBankEinzahlen instance = this;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!STARTED.get()) {
                    cancel();
                    return;
                }

                if (instance.amount > 1000) {
                    // if amount is bigger than 1000, add or remove 1k from faction bank and wait
                    p.sendChatMessage("/fbank " + interaction + " 1000");
                    instance.amount -= 1000;
                } else {
                    // otherwise add or remove the remainder and stop the task
                    p.sendChatMessage("/fbank " + interaction + " " + instance.amount);
                    STARTED.set(false);
                    cancel();

                    // send clock command
                    timer.schedule(new TimerTask() {
                        public void run() {
                            ClockCommand.sendClockMessage();
                        }
                    }, 200L);
                }
            }
        }, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));
        return true;
    }

    @SubscribeEvent
    public static void onBankFeedback(ClientChatReceivedEvent e) {
        if (!STARTED.get()) return;

        String msg = e.getMessage().getUnformattedText();
        Matcher taxesMatcher = FBANK_TAXES.matcher(msg);
        if (taxesMatcher.find()) {
            STARTED.set(false);
            ClockCommand.sendClockMessage();
            return;
        }

        if (msg.equals("[F-Bank] Du hast zu wenig Geld.") || msg.equals("Du befindest dich nicht in der Nähe eines Bankautomaten.")) {
            STARTED.set(false);
        }
    }

    public List<String> getTabCompletions(UPlayer p, String[] args) {
        return Arrays.asList("einzahlen", "auszahlen");
    }
}
