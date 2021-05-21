package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.TriadeSpeechModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ToggleTriadenSpeechCommand extends SpeechBaseCommand {

    public ToggleTriadenSpeechCommand() {
        super(new TriadeSpeechModifier(),
                "Du hast die Sprache der Triaden aktiviert.",
                "Du hast die Sprache der Triaden deaktiviert.");
    }

    @Command({"toggletriadenspeech", "togtriadenspeech", "toggletriaden", "togtriaden"})
    public boolean onCommand() {
        return super.onCommand();
    }
}
