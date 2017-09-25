package de.fuzzlemann.ucutils.utils.info;

import de.fuzzlemann.ucutils.utils.location.NavigationUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * @author Fuzzlemann
 */
public class FactionInfo {

    private final String fullName;
    private final String shortName;
    private final String hqPosition;
    private final String tasks;
    private final String factionType;
    private final String naviPoint;
    private final CommandInfo commandInfo;

    FactionInfo(String fullName, String shortName, String hqPosition, String tasks, String factionType, String naviPoint, CommandInfo commandInfo) {
        this.fullName = fullName;
        this.shortName = shortName;
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

    public ITextComponent constructClickableMessage(String command) {
        TextComponentString text = new TextComponentString("[" + shortName + "]");
        text.getStyle().setColor(TextFormatting.BLUE);

        TextComponentString hoverText = new TextComponentString("Klick mich!");
        hoverText.getStyle().setColor(TextFormatting.GREEN);

        text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        text.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        return text;
    }

    public ITextComponent constructFactionMessage() {
        TextComponentString headingBegin = new TextComponentString("\n  === ");
        headingBegin.getStyle().setColor(TextFormatting.DARK_GRAY);

        TextComponentString headingComponent = new TextComponentString(shortName);
        headingComponent.getStyle().setColor(TextFormatting.GOLD);

        TextComponentString headingEnd = new TextComponentString(" ===");
        headingEnd.getStyle().setColor(TextFormatting.DARK_GRAY);

        return headingBegin.appendSibling(headingComponent).appendSibling(headingEnd)
                .appendSibling(constructText("HQ", hqPosition))
                .appendSibling(constructText("Aufgaben", tasks))
                .appendSibling(constructText("Fraktionsart", factionType))
                .appendText("\n")
                .appendSibling(NavigationUtil.getNavigationText(naviPoint));
    }

    public ITextComponent constructCommandHelpMessage() {
        return commandInfo.constructMessage("Befehle von " + shortName);
    }

    private ITextComponent constructText(String preamble, String text) {
        return constructPreamble(preamble).appendSibling(new TextComponentString(text));
    }

    private ITextComponent constructPreamble(String preamble) {
        TextComponentString preambleComponent = new TextComponentString(preamble + ": ");
        preambleComponent.getStyle().setColor(TextFormatting.GREEN);

        return getPrefix().appendSibling(preambleComponent);
    }

    private ITextComponent getPrefix() {
        TextComponentString prefix = new TextComponentString("\n \u00bb ");
        prefix.getStyle().setColor(TextFormatting.GRAY);

        return prefix;
    }
}
