package de.fuzzlemann.ucutils.utils.navigation;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * @author Fuzzlemann
 */
public class NavigationUtil {

    public static ITextComponent getNavigationText(int x, int y, int z) {
        TextComponentString text = new TextComponentString("\u00bb ");
        text.getStyle().setColor(TextFormatting.GRAY);

        TextComponentString textMid = new TextComponentString("Route anzeigen");
        textMid.getStyle().setColor(TextFormatting.RED);

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textMid);
        textMid.getStyle().setHoverEvent(hoverEvent);

        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/navi " + x + "/" + y + "/" + z);
        textMid.getStyle().setClickEvent(clickEvent);

        return text.appendSibling(textMid);
    }
}
