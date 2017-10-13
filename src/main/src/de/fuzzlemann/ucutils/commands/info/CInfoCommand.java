package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.info.CommandDescription;
import de.fuzzlemann.ucutils.utils.info.CommandInfo;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CInfoCommand implements CommandExecutor {

    private static final CommandInfo COMMAND_INFO = new CommandInfo(new CommandDescription[]{
            new CommandDescription("/report", "Mit dem Befehl forderst du Hilfe von einem Supporter an"),
            new CommandDescription("/s", "Mit diesem Befehl schreist du. Diese Nachricht ist im Radius von 35 Bl\u00f6cke lesbar"),
            new CommandDescription("/forum", "Mit diesem Befehl verifizierst du deinen Forum Account"),
            new CommandDescription("/w", "Mit diesem Befehl fl\u00fcsterst du. Diese Nachricht ist im Radius von 8 Bl\u00f6cken lesbar"),
            new CommandDescription("/oos", "Mit diesem Befehl kannst du im Out-Of-Roleplay Chat schreiben"),
            new CommandDescription("/admins", "Mit diesem Befehl kannst du nachgucken, wer gerade im Admindienst ist"),
            new CommandDescription("/buy", "Mit diesem Befehl kannst du in verschiedenen L\u00e4den einkaufen"),
            new CommandDescription("/stats", "Hier kannst du deine Spieler Informationen nachgucken"),
            new CommandDescription("/inv", "Mit diesem Befehl kannst du in dein \"Drogenlager\" sehen"),
            new CommandDescription("/vote", "Mit dem Befehl kannst du die Vote-Seiten sehen, auf der du dann voten kannst, sodass du den Server unterst\u00fctzt sowie eine kleine Belohnung bekommst"),
            new CommandDescription("/fleader", "Damit kannst du die Fraktionsleader sehen, die online sind"),
            new CommandDescription("/ips", "Hier kannst du alle Seiten von UC auf einem Blick sehen"),
            new CommandDescription("/settings", "Konfiguriere ein paar Sachen (bspw. TeamSpeak, CP & Sounds)")}
    );

    @Override
    @Command(labels = {"cinfo", "commandinfo"})
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        p.sendMessage(COMMAND_INFO.constructMessage("Wichtige Befehle"));
        return true;
    }
}
