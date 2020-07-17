package de.fuzzlemann.ucutils.activitytest;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.common.activity.ActivityTestType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;

@Mod.EventBusSubscriber
public class ActivityTestMessageModifier {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent message = e.getMessage();
        String unformattedText = message.getUnformattedText();

        for (ActivityTestType testType : ActivityTestType.values()) {
            if (testType.getPattern() == null) continue;

            Matcher matcher = testType.getPattern().matcher(unformattedText);
            if (!matcher.find()) continue;

            if (matcher.groupCount() == 1) {
                String name = matcher.group(1);
                if (!name.equals(AbstractionLayer.getPlayer().getName())) return;
            }

            if (testType == ActivityTestType.KILLS) {
                if (message.getSiblings().get(0).getStyle().getColor() != TextFormatting.RED) return;
            }

            ActivityTestHandler.modifyTextComponent(testType, message);
            return;
        }
    }
}
