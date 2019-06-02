package de.fuzzlemann.ucutils.commands.time;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.FormatUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.sound.SoundUtil;
import de.fuzzlemann.ucutils.utils.sound.TimerSound;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class TimerCommand implements CommandExecutor, TabCompletion {
    private final Timer timer = new Timer();
    private final Map<Integer, Long> timers = new HashMap<>();
    private TimerSound currentTimerSound;
    private int timerID;

    @Override
    @Command(labels = "timer", usage = "/%label% [list/start/stop] (Zeit/ID)")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        switch (args[0].toLowerCase()) {
            case "start":
                if (args.length < 2) return false;

                long millis = FormatUtils.toMilliseconds(Arrays.copyOfRange(args, 1, args.length));
                if (millis <= 0) return false;

                int currentTimerID = ++timerID;

                Message.builder()
                        .prefix()
                        .of("Der Timer ").color(TextFormatting.GRAY).advance()
                        .of("#" + currentTimerID).color(TextFormatting.RED).advance()
                        .of(" wurde gestartet.").color(TextFormatting.GRAY).advance()
                        .send();

                timers.put(currentTimerID, System.currentTimeMillis() + millis);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (timers.remove(currentTimerID) == null) return;

                        Message.builder()
                                .prefix()
                                .of("Der Timer ").color(TextFormatting.GRAY).advance()
                                .of("#" + currentTimerID).color(TextFormatting.RED).advance()
                                .of(" ist abgelaufen. Vergangene Zeit: ").color(TextFormatting.GRAY).advance()
                                .messageParts(FormatUtils.formatMillisecondsToMessage(millis))
                                .of(".").color(TextFormatting.GRAY).advance()
                                .of(" [✓]").color(TextFormatting.GREEN).clickEvent(ClickEvent.Action.RUN_COMMAND, "/timer stopsound").advance()
                                .send();

                        p.playSound(SoundUtil.TIMER, 1, 1);

                        Main.MINECRAFT.addScheduledTask(() -> {
                            if (currentTimerSound != null) {
                                currentTimerSound.stop();
                            }

                            currentTimerSound = new TimerSound();
                            Minecraft.getMinecraft().getSoundHandler().playSound(currentTimerSound);
                        });
                    }
                }, millis);
                break;
            case "stop":
                if (args.length < 2) return false;

                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return false;
                }

                if (timers.remove(id) == null) {
                    TextUtils.error("Der Timer wurde nicht gefunden.");
                    return true;
                }

                TextUtils.error("Der Timer wurde gelöscht.");
                break;
            case "list":
                sendTimerList();
                break;
            case "stopsound":
                if (currentTimerSound != null) {
                    currentTimerSound.stop();
                }
                break;
            default:
                return false;
        }

        return true;
    }

    private void sendTimerList() {
        if (timers.isEmpty()) {
            TextUtils.error("Es ist derzeit kein Timer aktiv.");
            return;
        }

        Message.MessageBuilder builder = Message.builder();

        builder.of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Timer\n").color(TextFormatting.DARK_AQUA).advance();

        for (Map.Entry<Integer, Long> entry : timers.entrySet()) {
            int id = entry.getKey();
            long time = entry.getValue();

            long timeLeft = time - System.currentTimeMillis();

            builder.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                    .of("#" + id).color(TextFormatting.GRAY).advance()
                    .of(": ").color(TextFormatting.GRAY).advance()
                    .messageParts(FormatUtils.formatMillisecondsToMessage(timeLeft))
                    .of(" verbleibend").color(TextFormatting.GRAY).advance()
                    .of(" [✗]").color(TextFormatting.RED).clickEvent(ClickEvent.Action.RUN_COMMAND, "/timer stop " + id)
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Den Timer löschen", TextFormatting.RED)).advance()
                    .newLine();
        }

        builder.send();
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length == 1) return Arrays.asList("list", "start", "stop");

        return null;
    }
}
