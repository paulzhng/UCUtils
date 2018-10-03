package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CallReinforcementCommand implements CommandExecutor {

    private static final Pattern REINFORCEMENT_PATTERN = Pattern.compile("^(.+ ((?:\\[UC])*[a-zA-Z0-9_]+)): Ben\u00f6tige Verst\u00e4rkung! -> X: (-*\\d+) \\| Y: (-*\\d+) \\| Z: (-*\\d+)$");
    private static final Pattern ON_THE_WAY_PATTERN = Pattern.compile("^(.+ (?:\\[UC])*[a-zA-Z0-9_]+): ((?:\\[UC])*[a-zA-Z0-9_]+), ich bin zu deinem Verst\u00e4rkungsruf unterwegs! \\((\\d+) Meter entfernt\\)$");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        EntityPlayerSP p = Main.MINECRAFT.player;
        String msg = e.getMessage().getUnformattedText();

        Matcher reinforcementMatcher = REINFORCEMENT_PATTERN.matcher(msg);
        if (reinforcementMatcher.find()) {
            String fullName = reinforcementMatcher.group(1);
            String name = reinforcementMatcher.group(2);

            int posX = Integer.parseInt(reinforcementMatcher.group(3));
            int posY = Integer.parseInt(reinforcementMatcher.group(4));
            int posZ = Integer.parseInt(reinforcementMatcher.group(5));

            int distance = (int) p.getPosition().getDistance(posX, posY, posZ);

            boolean dChat = fullName.startsWith("FBI ") || fullName.startsWith("Polizei ") || fullName.startsWith("Rettungsdienst ");

            Message message = Message.builder()
                    .of(fullName).color(TextFormatting.DARK_GREEN).advance()
                    .of(" ben\u00f6tigt Unterst\u00fctzung bei X: " + posX + " | Y: " + posY + " | Z: " + posZ + "! (" + distance + " Meter entfernt)").color(TextFormatting.GREEN).advance().build();

            Message message2 = Message.builder()
                    .messageParts(NavigationUtil.getNavigationMessage(posX, posY, posZ).getMessageParts())
                    .of(" | ").color(TextFormatting.GRAY).advance()
                    .of("Unterwegs")
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.builder().message("Bescheid geben, dass man unterwegs ist").color(TextFormatting.RED).build())
                    .clickEvent(ClickEvent.Action.RUN_COMMAND, "/reinforcement ontheway " + name + " " + posX + " " + posY + " " + posZ + (dChat ? " -d" : ""))
                    .color(TextFormatting.RED).advance().build();

            p.sendMessage(message.toTextComponent());
            p.sendMessage(message2.toTextComponent());

            e.setCanceled(true);
            return;
        }

        Matcher onTheWayMatcher = ON_THE_WAY_PATTERN.matcher(msg);
        if (onTheWayMatcher.find()) {
            String senderFullName = onTheWayMatcher.group(1);
            String reinforcementSenderName = onTheWayMatcher.group(2);
            String distance = onTheWayMatcher.group(3);

            Message message = Message.builder()
                    .of(senderFullName).color(TextFormatting.DARK_GREEN).advance()
                    .of(" kommt zum Verst\u00e4rkungsruf von " + reinforcementSenderName + "! (" + distance + " Meter entfernt)").color(TextFormatting.GREEN).advance().build();

            p.sendMessage(message.toTextComponent());
            e.setCanceled(true);
        }
    }

    @Override
    @Command(labels = {"reinforcement", "callreinforcement", "verst\u00e4rkung"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        String type;
        if (args.length != 0) {
            switch (args[0].toLowerCase()) {
                case "-d":
                    type = "d";
                    break;
                case "ontheway":
                    if (args.length < 5) {
                        type = "f";
                        break;
                    }

                    String name = args[1];
                    int x;
                    int y;
                    int z;

                    try {
                        x = Integer.parseInt(args[2]);
                        y = Integer.parseInt(args[3]);
                        z = Integer.parseInt(args[4]);
                    } catch (Exception e) {
                        type = "f";
                        break;
                    }

                    String message;
                    if (args.length == 6 && args[5].equals("-d")) {
                        message = "/d " + name + ", ich bin zu deinem Verst\u00e4rkungsruf unterwegs! (" + (int) p.getPosition().getDistance(x, y, z) + " Meter entfernt)";
                    } else {
                        message = "/f " + name + ", ich bin zu deinem Verst\u00e4rkungsruf unterwegs! (" + (int) p.getPosition().getDistance(x, y, z) + " Meter entfernt)";
                    }

                    p.sendChatMessage(message);
                    return true;
                default:
                    type = "f";
            }
        } else {
            type = "f";
        }

        int posX = (int) p.posX;
        int posY = (int) p.posY;
        int posZ = (int) p.posZ;

        p.sendChatMessage("/" + type + " Ben\u00f6tige Verst\u00e4rkung! -> X: " + posX + " | Y: " + posY + " | Z: " + posZ);
        return true;
    }
}