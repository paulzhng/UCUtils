package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
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
public class MobileBlockListCommand {

    @Command({"mobileblocklist", "blocked", "mobileblocked"})
    public boolean onCommand() {
        List<String> blocked = MobileUtils.getBlockedPlayers();

        if (blocked.isEmpty()) {
            TextUtils.simpleMessage("Du hast bisher noch keinen Spieler blockiert.");
            return true;
        }

        Message.Builder builder = Message.builder();

        builder.of("» ").color(TextFormatting.DARK_GRAY).advance()
                .of("Blockierte Spieler\n").color(TextFormatting.DARK_AQUA).advance();

        TextComponentString unblockComponent = new TextComponentString("[✗]\n");
        unblockComponent.getStyle().setColor(TextFormatting.RED);

        for (String blockedPlayer : blocked) {
            builder.of("  * ").color(TextFormatting.DARK_GRAY).advance()
                    .of(blockedPlayer).color(TextFormatting.GRAY).advance()
                    .space()
                    .of("[✗]").color(TextFormatting.RED)
                    .hoverEvent(HoverEvent.Action.SHOW_TEXT, MessagePart.simple(blockedPlayer + " entblocken", TextFormatting.RED))
                    .clickEvent(ClickEvent.Action.RUN_COMMAND, "/mobileblock " + blockedPlayer).advance()
                    .newLine();
        }

        builder.send();
        return true;
    }
}
