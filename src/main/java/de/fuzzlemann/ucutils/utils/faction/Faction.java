package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;
import de.fuzzlemann.ucutils.utils.info.CommandDescription;
import de.fuzzlemann.ucutils.utils.info.CommandInfo;
import de.fuzzlemann.ucutils.utils.info.FactionInfo;

import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
@ParameterParser.At(FactionParser.class)
public enum Faction {
    UCPD("Police", constructFactionInfo("UnicaCity Police Department", "UCPD",
            "Das HQ befindet sich an der Papierfabrik",
            "Die Polizei hat die Aufgabe, für Recht und " +
                    "Ordnung auf UC zu sorgen. Unter Anderen können sie " +
                    "Wanteds vergeben und sind somit ein wichtiger " +
                    "Bestandteil vom Roleplay von UnicaCity",
            "Staatsfraktion", "Polizeistation",
            new CommandDescription("/wanteds", "Lasse dir alle gesuchten Spieler und deren Wantedpunkte anzeigen"),
            new CommandDescription("/wantedinfo", "Bekomme Wanted-Informationen über den Verbrecher"),
            new CommandDescription("/ramm", "Ramme gemeinsam mit einem Kollegen eine Haustür offen"),
            new CommandDescription("/tazer", "Lade dein Tazer auf"),
            new CommandDescription("/arrest", "Stecke einen Spieler in Handschellen ins Gefängnis"),
            new CommandDescription("/ticket", "Stelle einem Spieler ein Ticket aus"),
            new CommandDescription("/checkjailtime", "überprüfe wie lange ein Spieler im Gefängnis ist"),
            new CommandDescription("/strafzettel", "Erstelle ein Strafzettel für ein Falschparker"),
            new CommandDescription("/uncuff", "Entferne einen Spieler die Handschellen"),
            new CommandDescription("/unarrest", "Entlasse einen Spieler früher aus dem Gefängnis"),
            new CommandDescription("/klagen", "Liste alle offenen Anzeigen auf"),
            new CommandDescription("/d", "Schreibe in den Staatschat, in dem alle Staatsfraktionen schreiben können")
    )),
    FBI("FBI", constructFactionInfo("Federal Bureau of Investigation", "FBI",
            "Das HQ befindet sich zwischen dem Rotlichtviertel und Chinatown",
            "Das FBI hat die Aufgabe hochgradig gesuchte Verbrecher zu jagen sowie Geiselnahmen etc. zu koordinieren",
            "Staatsfraktion", "867/69/-63",
            new CommandDescription("/wanteds", "Lasse dir alle gesuchten Spieler und deren Wantedpunkte anzeigen"),
            new CommandDescription("/wantedinfo", "Bekomme Wanted-Informationen über den Verbrecher"),
            new CommandDescription("/arrest", "Stecke einen Spieler in Handschellen ins Gefängnis"),
            new CommandDescription("/uncuff", "Entferne einen Spieler die Handschellen"),
            new CommandDescription("/d", "Schreibe in den Staatschat, in dem alle Staatsfraktionen schreiben können"))),
    UCMD("Medic", constructFactionInfo("Rettungsdienst", "UCMD",
            "Die Medics besitzen ein großes Krankenhaus und eine Feuerwehrstation",
            "Die Medics haben die Aufgabe Leichen wiederzubeleben. " +
                    "Dazu haben sie die Aufgabe Operationen durchzuführen, " +
                    "Brände löschen und natürlich Patienten zu behandeln.",
            "Staatsfraktion", "Krankenhaus",
            new CommandDescription("/revive", "Belebe eine tote Person wieder"),
            new CommandDescription("/disease", "Untersuche eine Person nach Krankheiten"),
            new CommandDescription("/d", "Schreibe in den Staatschat, in dem alle Staatsfraktionen schreiben können"),
            new CommandDescription("/rezept", "Stelle einem Spieler ein Rezept aus"),
            new CommandDescription("/bloodtest", "Führe einen Bluttest durch"),
            new CommandDescription("/dropblood", "Stelle das Blut in euer Blutlager")
    )),
    NEWS("News", constructFactionInfo("News Agency", "News",
            "Die News hat eine kleine Zentrale gegenüber der Bank",
            "Licht! Kamera! Aktion! Eine spannende TV-Show erwartet uns! " +
                    "Perfekt durchgeplant von der News Agency: Die Zeitung und Ihre TV-Shows. " +
                    "Was wird uns wohl in der nächsten Ausgabe der Zeitung erwarten?",
            "Neutrale Fraktion", "-90/70/-357",
            new CommandDescription("/zeitung", "Damit bekommst du ein Buch aus dem Equip"),
            new CommandDescription("/tvshow", "Starte eine TV-Show")
    )),
    TERRORISTS("Terroristen", constructFactionInfo("Terroristen", "Terroristen",
            "Die Terroristen haben sich in einer Lagerhalle in der Nähe des Labors niedergelassen",
            "Die Terroristen können \"nur\" Bomben legen sowie Roleplay Anschläge machen.",
            "Neutrale Fraktion", "-55/60/494",
            new CommandDescription("/bomb", "Lege eine Bombe")
    )),
    HITMAN("Hitman", constructFactionInfo("Hitman", "Hitman",
            "Ihr HQ befindet sich in der Nähe vom Luigi's",
            "Wieder einmal ein Contract! Das ist genau Ihr Ding! Sie richten Ihre Aufträge ab und erledigen die Contracts!",
            "Neutrale Fraktion", "303/69/63",
            new CommandDescription("/contractlist", "Lasse dir alle Contracts anzeigen"),
            new CommandDescription("/delcontract", "Lösche ein Contract")
    )),
    CHURCH("Church", constructFactionInfo("Kirche", "Kirche",
            "Die Kirche hat sich neben dem Friedhof mit Hilfe Gottes Kraft eine Kirche errichtet",
            "Hallelujah! Mit der Bibel bewaffnet halten sie Ihre Gottesdienste und verkaufen Ablassbriefe! " +
                    "Dazu bringen sie verliebte Paare zusammen und vereinen sie mit Gottes Segen!",
            "Neutrale Fraktion", "Kirche",
            new CommandDescription("/ablassbrief", "Verkaufe einen Spieler ein Ablassbrief"),
            new CommandDescription("/marry", "Verheirate 2 Spieler"),
            new CommandDescription("/gottesdienst", "Eröffne einen Gottesdienst")
    )),
    MAFIA("Mafia", constructBadFactionInfo("La Cosa Nostra", "Cosa Nostra",
            "Sie besitzen eine große Villa in der Nähe des Waffenladen bzw. der Polizeistation",
            "Die Mafia besitzt die Aufgaben Drogen zu verkaufen, Waffen herzustellen und natürlich Roleplay zu betreiben.",
            "10/69/-464",
            new CommandDescription("/newgun", "Stelle Munition und Waffen her"),
            new CommandDescription("/sellgun", "Verkaufe deine erstellen Waffen und Munition")
    )),
    MEXICAN("Mexican", constructBadFactionInfo("Calderón Kartell", "Calderón",
            "Das Kartell besitzt eine Art Festung im mexikanischem Gebiet in der Nähe der Fahrschule",
            "Das Kartell hat neben dem Roleplay noch die Aufgabe Drogen zu verkaufen und die beliebten Briefbomben herzustellen.",
            "-90/70/-357",
            new CommandDescription("/brief", "Hole dir mit diesem Befehl einen Brief in der Postzentrale"),
            new CommandDescription("/briefbombe", "Stelle aus einem Brief und Schwarzpulver eine Briefbombe her"))
    ),
    GANG("Gang", constructBadFactionInfo("Juggalos", "Juggalos",
            "Die Gang besitzt einen kleinen Hof umrandet mit Häusern in der Nähe des Waffenladens",
            "Straßenrennen, Drogenverkauf und Geiselnahmen! Genau das sind die Aufgaben der Gang!",
            "-160/69/201",
            new CommandDescription("/race", "Startet ein Straßenrennen")
    )),
    KERZAKOV("Kerzakov", constructBadFactionInfo("Kerzakov Familie", "Kerzakov",
            "Die Kerzakov Basis liegt im KF Gebiet und besteht aus paar Lagerhallen",
            "Die Kerzakov kann momentan Drogen verkaufen, Geiselnahmen durchführen und gaaaanz viel Roleplay",
            "864/68/202"
    )),
    TRIADS("Triaden", constructBadFactionInfo("Triaden", "Triaden",
            "Das Hauptquartier der Triaden befindet sich ungefähr am Eingang von Chinatown",
            "Die Triaden - ein Wort - eine Fraktion - 1 Milliarden Chinesen",
            "1006/69/-101"
    )),
    OBRIEN("O_Brien", constructBadFactionInfo("O'Brien Familie", "O'Brien",
            "Das Hauptquartier der O'Briens befindet sich im irischen Viertel.",
            "Irische Butter.",
            "0/0/0"
    ));

    private static CommandInfo factionCommandInfo;
    private static CommandInfo badFactionCommandInfo;
    private final String factionKey;
    private final FactionInfo factionInfo;

    Faction(String factionKey, FactionInfo factionInfo) {
        this.factionKey = factionKey;
        this.factionInfo = factionInfo;
    }

    public static Faction getFactionEnum(String shortName) {
        return Arrays.stream(values())
                .filter(faction -> faction.getFactionInfo().getShortName().equalsIgnoreCase(shortName))
                .findFirst()
                .orElse(null);
    }

    private static FactionInfo constructBadFactionInfo(String fullName, String shortName, String hqPosition, String tasks, String naviPoint, CommandDescription... commandDescriptions) {
        if (badFactionCommandInfo == null) {
            badFactionCommandInfo = new CommandInfo(
                    new CommandDescription("/selldrug", "Verkauft Drogen an einem Spieler, das Geld geht in die Fraktionsbank"),
                    new CommandDescription("/drogenbank", "Lagert oder nimmt die Drogen aus der Drogenbank aus"),
                    new CommandDescription("/bl", "Lässt dir die Spieler auf der Blackliste anzeigen"),
                    new CommandDescription("/bl add", "Fügt einen Spieler zur Blacklist hinzu"),
                    new CommandDescription("/bl remove", "Entfernt den Spieler von der Blacklist"));
        }

        return constructFactionInfo(fullName, shortName, true, hqPosition, tasks, "Bad Fraktion", naviPoint,
                badFactionCommandInfo.createCopy().append(new CommandInfo(commandDescriptions)).getCommands());
    }

    private static FactionInfo constructFactionInfo(String fullName, String shortName, String hqPosition, String tasks, String factionType, String naviPoint, CommandDescription... commandDescriptions) {
        return constructFactionInfo(fullName, shortName, false, hqPosition, tasks, factionType, naviPoint, commandDescriptions);
    }

    private static FactionInfo constructFactionInfo(String fullName, String shortName, boolean badFrak, String hqPosition, String tasks, String factionType, String naviPoint, CommandDescription... commandDescriptions) {
        if (factionCommandInfo == null) {
            factionCommandInfo = new CommandInfo(
                    new CommandDescription("/equip", "Equippe dich"),
                    new CommandDescription("/f", "Schreibe in den Fraktionschat"),
                    new CommandDescription("/fbank", "Lässt dir den Stand der Fraktionsbank anzeigen"),
                    new CommandDescription("/fbank einzahlen", "Zahle Geld in die Fraktionsbank ein"),
                    new CommandDescription("/member", "Lässt dir alle Mitglieder deiner Fraktion anzeigen, die online sind"));
        }

        return new FactionInfo(fullName, shortName, badFrak, hqPosition, tasks, factionType, naviPoint,
                new CommandInfo(factionCommandInfo.createCopy().append(new CommandInfo(commandDescriptions)).getCommands()));
    }

    public String getFactionKey() {
        return factionKey;
    }

    public FactionInfo getFactionInfo() {
        return factionInfo;
    }
}
