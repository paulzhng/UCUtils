package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import lombok.SneakyThrows;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.SystemUtils;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class JShutdownCommand implements CommandExecutor {

    private static boolean shutdown;

    @SubscribeEvent
    @SneakyThrows
    public static void onChat(ClientChatReceivedEvent e) {
        if (!shutdown) return;
        if (!e.getMessage().getUnformattedText().equals("[Gef\u00e4ngnis] Du bist wieder frei!")) return;

        String shutdownCommand;

        if (SystemUtils.IS_OS_AIX) {
            shutdownCommand = "shutdown -Fh now";
        } else if (SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS) {
            shutdownCommand = "shutdown -y -i5 -gnow";
        } else if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_UNIX) {
            shutdownCommand = "shutdown -h now";
        } else if (SystemUtils.IS_OS_HP_UX) {
            shutdownCommand = "shutdown -hy now";
        } else if (SystemUtils.IS_OS_IRIX) {
            shutdownCommand = "shutdown -y -g now";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            shutdownCommand = "shutdown -s -t 0";
        } else {
            return;
        }

        Runtime.getRuntime().exec(shutdownCommand);
    }

    @Override
    @Command(labels = {"jshutdown", "jailshutdown"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        shutdown = !shutdown;

        ITextComponent text = shutdown
                ? TextUtils.simpleMessage("Du hast den Shutdown eingeleitet: Wenn du aus dem Knast bist, wird dein PC heruntergefahren.", TextFormatting.RED)
                : TextUtils.simpleMessage("Du hast den Shutdown abgebrochen", TextFormatting.GREEN);

        p.sendMessage(text);
        return true;
    }
}
