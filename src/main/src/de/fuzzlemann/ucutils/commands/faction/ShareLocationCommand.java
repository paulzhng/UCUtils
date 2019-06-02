package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ShareLocationCommand implements CommandExecutor {

    private static final Pattern SHARE_LOCATION_PATTERN = Pattern.compile("^(.+ (?:\\[UC])*[a-zA-Z0-9_]+): Positionsteilung für ([a-zA-Z0-9_, ]+)! -> X: (-*\\d+) \\| Y: (-*\\d+) \\| Z: (-*\\d+)$");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        ITextComponent messageComponent = e.getMessage();
        String msg = messageComponent.getUnformattedText();

        Matcher shareLocationMatcher = SHARE_LOCATION_PATTERN.matcher(msg);
        if (!shareLocationMatcher.find()) return;

        EntityPlayerSP p = Main.MINECRAFT.player;
        String playerName = p.getName();

        e.setCanceled(true);

        String names = shareLocationMatcher.group(2);

        List<String> nameList = Arrays.asList(names.split(", "));
        if (!nameList.contains(playerName)) return;

        String fullName = shareLocationMatcher.group(1);

        int posX = Integer.parseInt(shareLocationMatcher.group(3));
        int posY = Integer.parseInt(shareLocationMatcher.group(4));
        int posZ = Integer.parseInt(shareLocationMatcher.group(5));

        int distance = (int) p.getPosition().getDistance(posX, posY, posZ);

        Message.builder()
                .of(fullName).color(TextFormatting.DARK_GREEN).advance()
                .of(" hat seine Position mit dir geteilt! -> X: " + posX + " | Y: " + posY + " | Z: " + posZ + " (" + distance + " Meter entfernt)").color(TextFormatting.GREEN).advance()
                .newLine()
                .messageParts(NavigationUtil.getNavigationMessage(posX, posY, posZ).getMessageParts())
                .send();

        e.setCanceled(true);
    }

    @Override
    @Command(labels = {"sharelocation", "shareloc", "sloc"}, usage = "/sharelocation [Spieler...] (-d)")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String lastArgument = args[args.length - 1];
        boolean dChat = lastArgument.equalsIgnoreCase("-d");

        List<String> onlinePlayers = ForgeUtils.getOnlinePlayers();
        Set<String> playerNames = new LinkedHashSet<>();
        for (int i = 0; i < args.length; i++) {
            if (dChat && i == args.length - 1) continue;
            String argument = args[i];

            String foundPlayer = ForgeUtils.getMostMatching(onlinePlayers, argument);
            if (foundPlayer == null) continue;

            playerNames.add(foundPlayer);
        }

        if (playerNames.isEmpty()) {
            TextUtils.error("Der Spieler wurde nicht gefunden.");
            return true;
        }

        String playerString = String.join(", ", playerNames);
        String command = dChat ? "/d" : "/f";

        int posX = (int) p.posX;
        int posY = (int) p.posY;
        int posZ = (int) p.posZ;

        String fullCommand = command + " Positionsteilung für " + playerString + "! -> X: " + posX + " | Y: " + posY + " | Z: " + posZ;
        p.sendChatMessage(fullCommand);

        Message.builder()
                .prefix()
                .of("Du hast deine Position mit ").color(TextFormatting.GRAY).advance()
                .of(playerString).color(TextFormatting.BLUE).advance()
                .of(" geteilt.").color(TextFormatting.GRAY).advance()
                .send();
        return true;
    }
}
