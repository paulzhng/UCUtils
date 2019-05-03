package de.fuzzlemann.ucutils.commands.faction;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
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

    private static final Pattern REINFORCEMENT_PATTERN = Pattern.compile("^(.+ ((?:\\[UC])*[a-zA-Z0-9_]+)): Benötige Verstärkung! -> X: (-*\\d+) \\| Y: (-*\\d+) \\| Z: (-*\\d+)$");
    private static final Pattern ON_THE_WAY_PATTERN = Pattern.compile("^(.+ (?:\\[UC])*[a-zA-Z0-9_]+): ((?:\\[UC])*[a-zA-Z0-9_]+), ich bin zu deinem Verstärkungsruf unterwegs! \\((\\d+) Meter entfernt\\)$");

    private static ReinforcementType lastReinforcement;

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {
        EntityPlayerSP p = Main.MINECRAFT.player;
        ITextComponent messageComponent = e.getMessage();
        String msg = messageComponent.getUnformattedText();

        Matcher reinforcementMatcher = REINFORCEMENT_PATTERN.matcher(msg);
        if (reinforcementMatcher.find()) {
            String fullName = reinforcementMatcher.group(1);
            String name = reinforcementMatcher.group(2);

            int posX = Integer.parseInt(reinforcementMatcher.group(3));
            int posY = Integer.parseInt(reinforcementMatcher.group(4));
            int posZ = Integer.parseInt(reinforcementMatcher.group(5));

            int distance = (int) p.getPosition().getDistance(posX, posY, posZ);

            boolean dChat = messageComponent.getSiblings().get(0).getStyle().getColor() == TextFormatting.RED;

            Message.MessageBuilder builder = Message.builder();

            if (lastReinforcement != null && name.equals(lastReinforcement.getIssuer()) && System.currentTimeMillis() - lastReinforcement.getTime() < 1000) {
                builder.of(lastReinforcement.getType().getMessage()).color(TextFormatting.RED).advance().space();
            }

            Message message = builder.of(fullName).color(TextFormatting.DARK_GREEN).advance()
                    .of(" benötigt Unterstützung bei X: " + posX + " | Y: " + posY + " | Z: " + posZ + "! (" + distance + " Meter entfernt)").color(TextFormatting.GREEN).advance().build();

            Message message2 = Message.builder()
                    .messageParts(NavigationUtil.getNavigationMessage(posX, posY, posZ).getMessageParts())
                    .of(" | ").color(TextFormatting.GRAY).advance()
                    .of("Unterwegs")
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simpleMessagePart("Bescheid geben, dass man unterwegs ist", TextFormatting.RED))
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
                    .of(" kommt zum Verstärkungsruf von " + reinforcementSenderName + "! (" + distance + " Meter entfernt)").color(TextFormatting.GREEN).advance().build();

            p.sendMessage(message.toTextComponent());
            e.setCanceled(true);
            return;
        }

        for (Type type : Type.values()) {
            Pattern pattern = type.getPattern();
            if (pattern == null) continue;

            Matcher matcher = pattern.matcher(msg);
            if (!matcher.find()) continue;

            String name = matcher.group(1);

            lastReinforcement = new ReinforcementType(name, type);
            e.setCanceled(true);
        }
    }

    @Override
    @Command(labels = {"reinforcement", "callreinforcement", "verstärkung"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        String chatType;
        if (args.length != 0) {
            String argument = args[0].toLowerCase();
            switch (argument) {
                case "ontheway":
                    if (args.length < 5) {
                        chatType = "f";
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
                        chatType = "f";
                        break;
                    }

                    String message;
                    if (args.length == 6 && args[5].equals("-d")) {
                        message = "/d " + name + ", ich bin zu deinem Verstärkungsruf unterwegs! (" + (int) p.getPosition().getDistance(x, y, z) + " Meter entfernt)";
                    } else {
                        message = "/f " + name + ", ich bin zu deinem Verstärkungsruf unterwegs! (" + (int) p.getPosition().getDistance(x, y, z) + " Meter entfernt)";
                    }

                    p.sendChatMessage(message);
                    return true;
                default:
                    Type foundType = null;
                    for (Type t : Type.values()) {
                        if (t.getArgument().equalsIgnoreCase(argument)) {
                            foundType = t;
                            break;
                        }
                    }

                    if (foundType == null) {
                        chatType = "f";
                        break;
                    }

                    chatType = foundType.getChatType();

                    if (foundType.getMessage() != null)
                        p.sendChatMessage("/" + chatType + " " + foundType.getMessage());
            }
        } else {
            chatType = "f";
        }

        int posX = (int) p.posX;
        int posY = (int) p.posY;
        int posZ = (int) p.posZ;

        p.sendChatMessage("/" + chatType + " Benötige Verstärkung! -> X: " + posX + " | Y: " + posY + " | Z: " + posZ);
        return true;
    }

    private static class ReinforcementType {
        private final String issuer;
        private final Type type;
        private final long time;

        public ReinforcementType(String issuer, Type type) {
            this.issuer = issuer;
            this.type = type;
            this.time = System.currentTimeMillis();
        }

        public String getIssuer() {
            return issuer;
        }

        public Type getType() {
            return type;
        }

        public long getTime() {
            return time;
        }
    }

    private enum Type {
        D_CHAT("-d", "d", null),
        EMERGENCY("-e", "f", "Dringend!"),
        EMERGENCY_D("-ed", "d", "Dringend!"),
        MEDIC("-m", "d", "Medic benötigt!"),
        CORPSE_GUARDING("-lb", "d", "Leichenbewachung!");

        private final String argument;
        private final String chatType;
        private final String message;
        private final Pattern pattern;

        Type(String argument, String chatType, String message) {
            this.argument = argument;
            this.chatType = chatType;
            this.message = message;

            this.pattern = message != null ? Pattern.compile("^.+ ((?:\\[UC])*[a-zA-Z0-9_]+): " + message + "$") : null;
        }

        public String getArgument() {
            return argument;
        }

        public String getChatType() {
            return chatType;
        }

        public String getMessage() {
            return message;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }
}