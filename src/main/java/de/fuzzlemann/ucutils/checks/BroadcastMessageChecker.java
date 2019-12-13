package de.fuzzlemann.ucutils.checks;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import de.fuzzlemann.ucutils.common.BroadcastMessage;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.base.initializor.IInitializor;
import de.fuzzlemann.ucutils.base.initializor.InitMode;
import de.fuzzlemann.ucutils.base.initializor.Initializor;
import de.fuzzlemann.ucutils.utils.sound.SoundUtil;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.util.SoundEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "broadcastMessage", initMode = InitMode.DEFAULT)
public class BroadcastMessageChecker implements IInitializor {

    private final Timer timer = new Timer();
    private final Gson gson = new Gson();
    private final List<Integer> receivedBroadcasts = new ArrayList<>();

    @Override
    public void init() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForBroadcasts();
            }
        }, TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS.toMillis(30));
    }

    private void checkForBroadcasts() {
        try {
            String response = APIUtils.post("http://tomcat.fuzzlemann.de/factiononline/checkForBroadcasts",
                    "name", AbstractionLayer.getPlayer().getName());
            List<BroadcastMessage> broadcasts = gson.fromJson(response, new TypeToken<List<BroadcastMessage>>() {
            }.getType());

            for (BroadcastMessage broadcast : broadcasts) {
                if (broadcast.getTime() < System.currentTimeMillis()) continue;
                if (receivedBroadcasts.contains(broadcast.getID())) continue;
                receivedBroadcasts.add(broadcast.getID());

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        processBroadcast(broadcast);
                    }
                }, new Date(broadcast.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.LOGGER.error(e);
        }
    }

    private void processBroadcast(BroadcastMessage broadcastMessage) {
        String soundID = broadcastMessage.getSoundID();
        String message = broadcastMessage.getMessage();

        SoundEvent soundEvent = soundID == null ? null : SoundUtil.getSoundEvent(soundID);

        UPlayer player = AbstractionLayer.getPlayer();
        if (!player.isConnected()) return;

        if (soundEvent != null)
            player.playSound(soundEvent, 3, 1);
        TextUtils.fromLegacyText(message).send();
    }
}
