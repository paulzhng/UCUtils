package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.MexicanSpeechModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ToggleMexicanSpeechCommand extends SpeechBaseCommand {

    public ToggleMexicanSpeechCommand() {
        super(new MexicanSpeechModifier(),
                "Du hast die Sprache des Calderon Kartell aktiviert.",
                "Du hast die Sprache des Calderon Kartell deaktiviert.");
    }

    @Command(value = {"togglemexicanspeech", "togmexicanspeech", "togglemexican", "togmexican"})
    public boolean onCommand() {
        return super.onCommand();
    }
}
