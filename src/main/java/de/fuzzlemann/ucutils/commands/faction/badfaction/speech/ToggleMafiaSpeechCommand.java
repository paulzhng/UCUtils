package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.CosaNostraSpeechModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ToggleMafiaSpeechCommand extends SpeechBaseCommand {

    public ToggleMafiaSpeechCommand() {
        super(new CosaNostraSpeechModifier(),
                "Du hast die Sprache der La Cosa Nostra aktiviert.",
                "Du hast die Sprache der La Cosa Nostra deaktiviert.");
    }

    @Command(value = {"togglemafiaspeech", "togmafiaspeech", "togglemafia", "togmafia"})
    public boolean onCommand() {
        return super.onCommand();
    }
}
