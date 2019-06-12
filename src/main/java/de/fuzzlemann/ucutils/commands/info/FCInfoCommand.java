package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.info.FactionInfo;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class FCInfoCommand {

    @Command({"fcinfo", "factioncommandinfo", "fcommandinfo", "factioncinfo"})
    public boolean onCommand(UPlayer p, @CommandParam(joinStart = true, required = false, defaultValue = CommandParam.NULL) Faction faction) {
        if (faction != null) {
            p.sendMessage(faction.getFactionInfo().constructCommandHelpMessage());
            return true;
        }

        TextComponentString text = new TextComponentString("");

        for (Faction f : Faction.values()) {
            FactionInfo factionInfo = f.getFactionInfo();

            text.appendText("\n").appendSibling(factionInfo.constructClickableMessage("/fcinfo " + factionInfo.getShortName()));
        }

        p.sendMessage(text);
        return true;
    }
}
