package de.fuzzlemann.ucutils.utils.info;

import de.fuzzlemann.ucutils.base.text.Message;
import de.fuzzlemann.ucutils.base.text.MessagePart;
import de.fuzzlemann.ucutils.utils.location.navigation.NavigationUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * @author Fuzzlemann
 */
public class FactionInfo {

    private final String fullName;
    private final String shortName;
    private final boolean badFrak;
    private final String hqPosition;
    private final String tasks;
    private final String factionType;
    private final String naviPoint;
    private final CommandInfo commandInfo;

    public FactionInfo(String fullName, String shortName, boolean badFrak, String hqPosition, String tasks, String factionType, String naviPoint, CommandInfo commandInfo) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.badFrak = badFrak;
        this.hqPosition = hqPosition;
        this.tasks = tasks;
        this.factionType = factionType;
        this.naviPoint = naviPoint;
        this.commandInfo = commandInfo;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isBadFrak() {
        return badFrak;
    }

    public String getHqPosition() {
        return hqPosition;
    }

    public String getTasks() {
        return tasks;
    }

    public String getFactionType() {
        return factionType;
    }

    public String getNaviPoint() {
        return naviPoint;
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    public Message constructClickableMessage(String command) {
        return Message.builder()
                .of("[" + shortName + "]").color(TextFormatting.BLUE)
                .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple("Informationen abrufen", TextFormatting.GREEN))
                .clickEvent(ClickEvent.Action.RUN_COMMAND, command).advance()
                .build();
    }

    public Message constructFactionMessage() {
        return Message.builder()
                .newLine()
                .of("  === ").color(TextFormatting.DARK_GRAY).advance()
                .of(shortName).color(TextFormatting.GOLD).advance()
                .of(" ===").color(TextFormatting.DARK_GRAY).advance()
                .messageParts(constructText("HQ", hqPosition).getMessageParts())
                .messageParts(constructText("Aufgaben", tasks).getMessageParts())
                .messageParts(constructText("Fraktionsart", factionType).getMessageParts())
                .newLine()
                .messageParts(NavigationUtil.getNavigationMessage(naviPoint).getMessageParts())
                .build();
    }

    public Message constructCommandHelpMessage() {
        return commandInfo.constructMessage("Befehle von " + shortName);
    }

    private Message constructText(String preamble, String text) {
        return Message.builder()
                .messageParts(constructPreamble(preamble).getMessageParts())
                .of(text).color(TextFormatting.GRAY).advance()
                .build();
    }

    private Message constructPreamble(String preamble) {
        return Message.builder()
                .messageParts(getPrefix())
                .of(preamble + ": ").color(TextFormatting.GREEN).advance()
                .build();
    }

    private MessagePart getPrefix() {
        return MessagePart.simple("\n » ", TextFormatting.GRAY);
    }
}
