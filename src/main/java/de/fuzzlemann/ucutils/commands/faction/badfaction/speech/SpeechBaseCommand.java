package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;

import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.SpeechModifier;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.SpeechModifyUtil;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public abstract class SpeechBaseCommand {

    private static final Map<Class<? extends SpeechModifier>, SpeechModifier> activatedSpeechModifiers = new HashMap<>();

    private final SpeechModifier speechModifier;
    private final String activatedMessage;
    private final String deactivatedMessage;

    SpeechBaseCommand(SpeechModifier speechModifier, String activatedMessage, String deactivatedMessage) {
        this.speechModifier = speechModifier;
        this.activatedMessage = activatedMessage;
        this.deactivatedMessage = deactivatedMessage;
    }

    @SubscribeEvent
    public static void onChat(ClientChatEvent e) {
        String message = e.getMessage();

        for (SpeechModifier speechModifier : activatedSpeechModifiers.values()) {
            String modifiedMessage = SpeechModifyUtil.modifyString(message, speechModifier);
            if (modifiedMessage == null) continue;

            message = modifiedMessage;
        }

        if (message != null)
            e.setMessage(message);
    }

    public boolean onCommand() {
        Class<? extends SpeechModifier> clazz = speechModifier.getClass();
        if (activatedSpeechModifiers.remove(clazz) != null) {
            TextUtils.simpleMessage(deactivatedMessage);
        } else {
            activatedSpeechModifiers.put(clazz, speechModifier);
            TextUtils.simpleMessage(activatedMessage);
        }

        return true;
    }
}
