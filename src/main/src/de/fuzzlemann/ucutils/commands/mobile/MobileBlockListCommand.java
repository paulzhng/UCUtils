package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class MobileBlockListCommand implements CommandExecutor {

    @Override
    @Command(labels = {"mobileblocklist", "blocked", "mobileblocked"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        List<String> blocked = MobileUtils.getBlockedPlayers();

        if (blocked.isEmpty()) {
            TextComponentString text = new TextComponentString("Du hast keinen Spieler blockiert.");
            text.getStyle().setColor(TextFormatting.GREEN);

            p.sendMessage(text);
            return true;
        }

        TextComponentString text = new TextComponentString("\u00bb");
        text.getStyle().setColor(TextFormatting.GOLD);

        TextComponentString textMid = new TextComponentString(" Geblockte Spieler\n");
        textMid.getStyle().setColor(TextFormatting.DARK_PURPLE);
        text.appendSibling(textMid);

        TextComponentString unblockComponent = new TextComponentString("[\u2717]\n");
        unblockComponent.getStyle().setColor(TextFormatting.RED);

        for (String blockedPlayer : blocked) {
            TextComponentString blockedComponent = new TextComponentString("  * " + blockedPlayer + " ");
            blockedComponent.getStyle().setColor(TextFormatting.GRAY);

            TextComponentString hoverText = new TextComponentString("Entblock " + blockedPlayer);
            hoverText.getStyle().setColor(TextFormatting.GREEN);

            TextComponentString copiedUnblockComponent = unblockComponent.createCopy();

            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText);
            copiedUnblockComponent.getStyle().setHoverEvent(hoverEvent);

            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mobileblock " + blockedPlayer);
            copiedUnblockComponent.getStyle().setClickEvent(clickEvent);

            text.appendSibling(blockedComponent).appendSibling(copiedUnblockComponent);
        }

        p.sendMessage(text);
        return true;
    }
}
