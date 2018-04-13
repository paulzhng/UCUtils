package de.fuzzlemann.ucutils.events;

import de.fuzzlemann.ucutils.Main;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class VoteMessageModifierEventHandler {

    private static final Pattern MINECRAFT_SERVER_EU_PATTERN = Pattern.compile(" {2}\u00a77\u00bb\u00a7c \u00a7cwww.minecraft-server.eu| {2}\u00a77\u00bb\u00a7a \u00a7awww.minecraft-server.eu");
    private static final Pattern MINECRAFT_SERVERLIST_EU_PATTERN = Pattern.compile(" {2}\u00a77\u00bb\u00a7c \u00a7cwww.minecraft-serverlist.net| {2}\u00a77\u00bb\u00a7a \u00a7awww.minecraft-serverlist.net");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent textComponent = e.getMessage();
        String text = textComponent.getUnformattedText();

        String voteURL;
        if (MINECRAFT_SERVER_EU_PATTERN.matcher(text).find()) {
            voteURL = "https://minecraft-server.eu/vote/index/109483";
        } else if (MINECRAFT_SERVERLIST_EU_PATTERN.matcher(text).find()) {
            voteURL = "https://www.minecraft-serverlist.net/vote/24860";
        } else {
            return;
        }

        voteURL += "/" + Main.MINECRAFT.player.getName();

        List<ITextComponent> siblings = textComponent.getSiblings();
        siblings.get(siblings.size() - 1).getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, voteURL));
    }
}
