package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.KerzakovSpeechModifier;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ToggleKerzakovSpeechCommand extends SpeechBaseCommand {

    public ToggleKerzakovSpeechCommand() {
        super(new KerzakovSpeechModifier(),
                "Du hast die Sprache der Kerzakov Familie aktiviert.",
                "Du hast die Sprache der Kerzakov Familie deaktiviert.");
    }

    @Override
    @Command(labels = {"togglekerzakovspeech", "togkerzakovspeech", "togglekerzakov", "togkerzakov"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        return super.onCommand(p, args);
    }
}
