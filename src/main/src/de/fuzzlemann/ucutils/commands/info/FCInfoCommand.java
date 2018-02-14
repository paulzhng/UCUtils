package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.info.FactionInfo;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class FCInfoCommand implements CommandExecutor {

    @Override
    @Command(labels = {"fcinfo", "factioncommandinfo", "fcommandinfo", "factioncinfo"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) {
            TextComponentString text = new TextComponentString("");

            for (Faction faction : Faction.values()) {
                FactionInfo factionInfo = faction.getFactionInfo();

                text.appendText("\n").appendSibling(factionInfo.constructClickableMessage("/fcinfo " + factionInfo.getShortName()));
            }

            p.sendMessage(text);
            return true;
        }

        Faction faction = Faction.getFactionEnum(args[0]);

        if (faction == null) {
            TextUtils.error("Die Fraktion wurde nicht gefunden", p);
            return true;
        }

        p.sendMessage(faction.getFactionInfo().constructCommandHelpMessage());
        return true;
    }
}
