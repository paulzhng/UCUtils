package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.common.CustomNaviPoint;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class NaviCommand implements CommandExecutor, TabCompletion {

    private static final Pattern NAVI_DELETED_PATTERN = Pattern.compile("^\\[Navi] Du hast deine Route gelöscht.$");
    private static final Timer TIMER = new Timer();
    private static CompletableFuture<Boolean> future;
    private static long lastCommand;

    @Override
    @Command(labels = "navi")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) {
            passToServer(p, args);
            return true;
        }

        CustomNaviPoint naviPoint = NavigationUtil.getNaviPoint(args[0]);
        if (naviPoint == null) {
            passToServer(p, args);
            return true;
        }

        lastCommand = System.currentTimeMillis();
        future = new CompletableFuture<>();

        p.sendChatMessage("/navi " + naviPoint.getX() + "/" + naviPoint.getY() + "/" + naviPoint.getZ());

        new Thread(() -> {
            if (future == null) return;

            try {
                Boolean bool = future.get(500, TimeUnit.MILLISECONDS);
                if (bool != null && !bool) return;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException ignored) {
            }

            Message.builder()
                    .of("[").color(TextFormatting.DARK_GRAY).advance()
                    .of("Navi").color(TextFormatting.YELLOW).advance()
                    .of("]").color(TextFormatting.DARK_GRAY).advance()
                    .of(" Dir wird nun die Route zum Punkt ").color(TextFormatting.GOLD).advance()
                    .of(naviPoint.getNames().get(0)).color(TextFormatting.GOLD).bold().advance()
                    .of(" angezeigt.").color(TextFormatting.GOLD).advance()
                    .send();

            future = null;
        }).start();

        return true;
    }

    private void passToServer(EntityPlayerSP p, String[] args) {
        p.sendChatMessage("/navi " + String.join(" ", args));
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        String input = args[args.length - 1];

        List<String> naviPointNames = new ArrayList<>();
        for (CustomNaviPoint naviPoint : NavigationUtil.NAVI_POINTS) {
            for (String name : naviPoint.getNames()) {
                naviPointNames.add(name.replace(' ', '-'));
            }
        }

        naviPointNames.removeIf(naviPoint -> !naviPoint.toLowerCase().startsWith(input));
        Collections.sort(naviPointNames);

        return naviPointNames;
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        if (System.currentTimeMillis() - lastCommand > 500) {
            if (future != null) {
                future.complete(true);
                future = null;
            }

            return;
        }

        String message = e.getMessage().getUnformattedText();
        if (!NAVI_DELETED_PATTERN.matcher(message).find()) return;

        future.complete(false);
        future = null;
    }
}
