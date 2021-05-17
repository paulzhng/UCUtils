package de.fuzzlemann.ucutils.commands.faction.badfaction.speech;


import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.faction.badfaction.speech.ObrienSpeechModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ToggleObrienSpeechCommand extends SpeechBaseCommand  {

    public ToggleObrienSpeechCommand() {
        super(new ObrienSpeechModifier(),
                "Du hast die Sprache der Obrien Familie aktiviert",
                "Du hast die Sprache der Obrien Familie deaktiviert");
    }

    @Command(value = {"toggleobrien", "toggleobrienspeech"})
    public boolean onCommand() {
        return super.onCommand();
    }

}
