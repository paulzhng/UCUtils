package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.MafiaSpeechModifier;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.SpeechModifier;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.SpeechModifyUtil;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**d
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
@SideOnly(Side.CLIENT)
public class ToggleMafiaSpeechCommand implements CommandExecutor {

    private static boolean activated;
    private static final SpeechModifier MODIFIER = new MafiaSpeechModifier();

    @Override
    @Command(labels = {"togglemafiaspeech", "togmafiaspeech", "togglemafia", "togmafia"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        activated = !activated;

        ITextComponent text = activated
                ? TextUtils.simpleMessage("Du hast die Mafiasprache eingeschalten.", TextFormatting.GREEN)
                : TextUtils.simpleMessage("Du hast die Mafiasprache ausgeschalten.", TextFormatting.RED);

        p.sendMessage(text);
        return true;
    }

    @SubscribeEvent
    public static void onChat(ClientChatEvent e) {
        if (!activated) return;

        String message = e.getMessage();
        String modified = SpeechModifyUtil.modifyString(message, MODIFIER);

        if (modified != null)
            e.setMessage(modified);
    }
}
