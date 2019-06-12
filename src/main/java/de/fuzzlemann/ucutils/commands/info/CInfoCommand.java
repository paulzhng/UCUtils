package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.info.CommandDescription;
import de.fuzzlemann.ucutils.utils.info.CommandInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CInfoCommand {

    private static final CommandInfo COMMAND_INFO = new CommandInfo(
            new CommandDescription("/report", "Mit dem Befehl forderst du Hilfe von einem Supporter an"),
            new CommandDescription("/s", "Mit diesem Befehl schreist du. Diese Nachricht ist im Radius von 35 Blöcke lesbar"),
            new CommandDescription("/forum", "Mit diesem Befehl verifizierst du deinen Forum Account"),
            new CommandDescription("/w", "Mit diesem Befehl flüsterst du. Diese Nachricht ist im Radius von 8 Blöcken lesbar"),
            new CommandDescription("/oos", "Mit diesem Befehl kannst du im Out-Of-Roleplay Chat schreiben"),
            new CommandDescription("/admins", "Mit diesem Befehl kannst du nachgucken, wer gerade im Admindienst ist"),
            new CommandDescription("/buy", "Mit diesem Befehl kannst du in verschiedenen Läden einkaufen"),
            new CommandDescription("/stats", "Hier kannst du deine Spieler Informationen nachgucken"),
            new CommandDescription("/inv", "Mit diesem Befehl kannst du in dein \"Drogenlager\" sehen"),
            new CommandDescription("/vote", "Mit dem Befehl kannst du die Vote-Seiten sehen, auf der du dann voten kannst, sodass du den Server unterstützt sowie eine kleine Belohnung bekommst"),
            new CommandDescription("/fleader", "Damit kannst du die Fraktionsleader sehen, die online sind"),
            new CommandDescription("/ips", "Hier kannst du alle Seiten von UC auf einem Blick sehen"),
            new CommandDescription("/settings", "Konfiguriere ein paar Sachen (bspw. TeamSpeak, CP & Sounds)")
    );

    @Command({"cinfo", "commandinfo"})
    public boolean onCommand(UPlayer p) {
        p.sendMessage(COMMAND_INFO.constructMessage("Wichtige Befehle"));
        return true;
    }
}
