package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fuzzlemann.ucutils.commands.time.ClockCommand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AFbankEinzahlen implements TabCompletion {
    
    private int betrag;

    private final Timer timer = new Timer();

    private static final AtomicBoolean started = new AtomicBoolean();
    private static boolean stop = false;

    private static final Pattern FBANK_TAXES_PATTERN = Pattern.compile("^\\[F-Bank] (?:\\[UC])*([a-zA-Z0-9_]+) hat (\\d+)\\$ \\((-\\d)\\$\\) .+ Fraktionsbank ([a-zA-Z_]+)\\.$");

    @Command(value = {"afbank"}, usage = "/%label% [Interaktion] [Betrag]")
    public boolean onCommand(final UPlayer p, final String interaktion, int amount) {
        if (started.get())
            return true;

        if (!interaktion.equalsIgnoreCase("einzahlen") && !interaktion.equalsIgnoreCase("auszahlen")) {
            TextUtils.error("Bitte wähle zwischen 'einzahlen' und 'auszahlen'");
            return true;
        }

        if (interaktion.equalsIgnoreCase("einzahlen")) {
            p.sendChatMessage("/fbank einzahlen 4");
        } else {
            p.sendChatMessage("/fbank auszahlen 4");
        }
        betrag = amount - 4;

        stop = false;
        started.set(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (stop) {
                    cancel();
                    return;
                }

                if (AFbankEinzahlen.this.betrag > 1000) {
                    p.sendChatMessage("/fbank " + interaktion + " 1000");
                    betrag = betrag - 1000;
                } else {
                    p.sendChatMessage("/fbank " + interaktion + " " + betrag);
                    started.set(false);
                    cancel();

                    timer.schedule(new TimerTask() {
                        public void run() {
                            ClockCommand.sendClockMessage();
                        }
                    }, 200L);
                }
            }
        }, 1000L, 1000L);
        return true;
    }

    @SubscribeEvent
    public static void onBankFeedback(ClientChatReceivedEvent e) {
        String msg = e.getMessage().getUnformattedText();
        Matcher taxesMatcher = FBANK_TAXES_PATTERN.matcher(msg);

        if (started.get() && (taxesMatcher.find() || msg.equals("[F-Bank] Du hast zu wenig Geld.") || msg.equals("Du befindest dich nicht in der Nähe eines Bankautomaten."))) {
            stop = true;
            started.set(false);
        }
    }

    public List<String> getTabCompletions(UPlayer p, String[] args) {
        List<String> list = Arrays.asList(new String[]{"einzahlen", "auszahlen"});
        return list;
    }
}
