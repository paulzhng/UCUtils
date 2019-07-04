package de.fuzzlemann.ucutils.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class EventNotificationEventHandler {

    @SubscribeEvent
    public static void onUnicaJoin(ClientChatReceivedEvent e) {
        if (UCUtilsConfig.apiKey.isEmpty()) return;

        String unformattedText = e.getMessage().getUnformattedText();
        if (!unformattedText.equals("Willkommen zurück!")) return;

        new Thread(() -> {
            try {
                String response = APIUtils.postAuthenticated("http://tomcat.fuzzlemann.de/factiononline/checkevents");
                if (response == null || response.isEmpty()) return;

                JsonElement responseElement = new JsonParser().parse(response);
                if (responseElement == null) return;
                if (responseElement.isJsonNull()) return;

                JsonObject responseObject = responseElement.getAsJsonObject();
                String startDate = responseObject.get("startDate").getAsJsonPrimitive().getAsString();
                String eventType = responseObject.get("eventType").getAsJsonPrimitive().getAsString();
                String comment = responseObject.get("comment").getAsJsonPrimitive().getAsString().trim();

                Message.Builder builder = Message.builder().of("Ereignis: " + eventType + " | Am " + startDate).color(TextFormatting.RED).advance();

                if (!comment.isEmpty()) {
                    builder.of(" | Kommentar: " + comment).advance();
                }

                builder.send();
            } catch (Exception exc) {
                Logger.LOGGER.catching(exc);
            }
        }).start();
    }
}
