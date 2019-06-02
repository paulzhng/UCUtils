package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.TriadsSpeechModifier;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ToggleTriadenSpeechCommand extends SpeechBaseCommand {

    public ToggleTriadenSpeechCommand() {
        super(new TriadsSpeechModifier(),
                "Du hast die Sprache der Triaden aktiviert.",
                "Du hast die Sprache der Triaden deaktiviert.");
    }

    @Override
    @Command(labels = {"toggletriadenspeech", "togtriadenspeech", "toggletriaden", "togtriaden"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        return super.onCommand(p, args);
    }
}
