package de.fuzzlemann.ucutils.events.teamspeak;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientVariableCommand;
import de.fuzzlemann.ucutils.teamspeak.events.ClientMovedEvent;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionHandler;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.sound.SoundUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class WaitingRoomEventHandler {

    private static final Table<Integer, Integer, Long> COOLDOWN_TABLE = HashBasedTable.create();

    @SubscribeEvent
    public static void onClientMoved(ClientMovedEvent e) {
        if (Main.MINECRAFT.player == null) return;

        boolean supportNotification = UCUtilsConfig.notifyWaitingSupport;
        boolean publicNotification = UCUtilsConfig.notifyWaitingPublic;
        if (!supportNotification && !publicNotification) return;

        int targetChannelID = e.getTargetChannelID();
        boolean support;
        if (targetChannelID == 30) {
            if (!supportNotification) return;
            support = true;
        } else if (Faction.getFactionOfPlayer() != null && targetChannelID == Faction.getFactionOfPlayer().getPublicChannelID()) {
            if (!publicNotification) return;
            support = false;
        } else {
            return;
        }

        int clientID = e.getClientID();

        Map<Integer, Long> row = COOLDOWN_TABLE.row(clientID);
        Long lastTime = row.get(targetChannelID);
        if (lastTime != null && (System.currentTimeMillis() - lastTime) < TimeUnit.MINUTES.toMillis(5)) return;

        COOLDOWN_TABLE.put(clientID, targetChannelID, System.currentTimeMillis());

        new Thread(() -> {
            ClientVariableCommand.Response response = new ClientVariableCommand(clientID).getResponse();
            String name = response.getDescription();

            Message.Builder builder = Message.builder()
                    .prefix()
                    .of(name).color(TextFormatting.BLUE).advance()
                    .space();

            if (support) {
                builder.of("hat das Wartezimmer betreten.").color(TextFormatting.GRAY).advance();
            } else {
                builder.of("hat den Öffentlich-Channel betreten.").color(TextFormatting.GRAY).advance();
            }

            builder.send();

            Main.MINECRAFT.addScheduledTask(() -> AbstractionHandler.getInstance().getPlayer().playSound(Objects.requireNonNull(SoundUtil.getSoundEvent("block.note.pling")), 1, 1));
        }).start();
    }
}
