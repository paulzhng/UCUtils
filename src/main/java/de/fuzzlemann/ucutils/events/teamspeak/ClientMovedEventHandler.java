package de.fuzzlemann.ucutils.events.teamspeak;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientVariableCommand;
import de.fuzzlemann.ucutils.teamspeak.events.ClientMovedEvent;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionHandler;
import de.fuzzlemann.ucutils.utils.sound.SoundUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class ClientMovedEventHandler {

    private static final Cache<Long, Integer> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    @SubscribeEvent
    public static void onClientMoved(ClientMovedEvent e) {
        if (!UCUtilsConfig.notifyWaitingSupport) return;

        int targetChannelID = e.getTargetChannelID();
        if (targetChannelID != 30) return;

        int clientID = e.getClientID();

        if (CACHE.asMap().containsValue(clientID)) return;
        CACHE.put(System.currentTimeMillis(), clientID);

        new Thread(() -> {
            ClientVariableCommand.Response response = new ClientVariableCommand(clientID).execute().getResponse();
            String name = response.getDescription();

            Message.builder()
                    .prefix()
                    .of(name).color(TextFormatting.BLUE).advance()
                    .space()
                    .of("hat den Support Wartezimmer betreten.").color(TextFormatting.GRAY).advance()
                    .send();

            Main.MINECRAFT.addScheduledTask(() -> AbstractionHandler.getInstance().getPlayer().playSound(Objects.requireNonNull(SoundUtil.getSoundEvent("block.note.pling")), 1, 1));
        }).start();
    }
}
