package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.utils.info.CommandDescription;
import de.fuzzlemann.ucutils.utils.info.CommandInfo;
import de.fuzzlemann.ucutils.utils.info.FactionInfo;

import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
public enum Faction {
    UCPD("Police", constructFactionInfo("UnicaCity Police Department", "UCPD",
            "Das HQ befindet sich an der Papierfabrik",
            "Die Polizei hat die Aufgabe, f\u00fcr Recht und " +
                    "Ordnung auf UC zu sorgen. Unter Anderen k\u00f6nnen sie " +
                    "Wanteds vergeben und sind somit ein wichtiger " +
                    "Bestandteil vom Roleplay von UnicaCity",
            "Staatsfraktion", "Polizeistation",
            new CommandDescription("/wanteds", "Lasse dir alle gesuchten Spieler und deren Wantedpunkte anzeigen"),
            new CommandDescription("/wantedinfo", "Bekomme Wanted-Informationen \u00fcber den Verbrecher"),
            new CommandDescription("/ramm", "Ramme gemeinsam mit einem Kollegen eine Haust\u00fcr offen"),
            new CommandDescription("/tazer", "Lade dein Tazer auf"),
            new CommandDescription("/arrest", "Stecke einen Spieler in Handschellen ins Gef\u00e4ngnis"),
            new CommandDescription("/ticket", "Stelle einem Spieler ein Ticket aus"),
            new CommandDescription("/checkjailtime", "\u00fcberpr\u00fcfe wie lange ein Spieler im Gef\u00e4ngnis ist"),
            new CommandDescription("/strafzettel", "Erstelle ein Strafzettel f\u00fcr ein Falschparker"),
            new CommandDescription("/uncuff", "Entferne einen Spieler die Handschellen"),
            new CommandDescription("/unarrest", "Entlasse einen Spieler fr\u00fcher aus dem Gef\u00e4ngnis"),
            new CommandDescription("/klagen", "Liste alle offenen Anzeigen auf"),
            new CommandDescription("/d", "Schreibe in den Staatschat, in dem alle Staatsfraktionen schreiben k\u00f6nnen")
    )),
    FBI("FBI", constructFactionInfo("Federal Bureau of Investigation", "FBI",
            "Das HQ befindet sich zwischen dem Rotlichtviertel und Chinatown",
            "Das FBI hat die Aufgabe hochgradig gesuchte Verbrecher zu jagen sowie Geiselnahmen etc. zu koordinieren",
            "Staatsfraktion", "867/69/-63",
            new CommandDescription("/wanteds", "Lasse dir alle gesuchten Spieler und deren Wantedpunkte anzeigen"),
            new CommandDescription("/wantedinfo", "Bekomme Wanted-Informationen \u00fcber den Verbrecher"),
            new CommandDescription("/arrest", "Stecke einen Spieler in Handschellen ins Gef\u00e4ngnis"),
            new CommandDescription("/uncuff", "Entferne einen Spieler die Handschellen"),
            new CommandDescription("/d", "Schreibe in den Staatschat, in dem alle Staatsfraktionen schreiben k\u00f6nnen"))),
    UCMD("Medic", constructFactionInfo("Rettungsdienst", "UCMD",
            "Die Medics besitzen ein gro\u00dfes Krankenhaus und eine Feuerwehrstation",
            "Die Medics haben die Aufgabe Leichen wiederzubeleben. " +
                    "Dazu haben sie die Aufgabe Operationen durchzuf\u00fchren, " +
                    "Br\u00e4nde l\u00f6schen und nat\u00fcrlich Patienten zu behandeln.",
            "Staatsfraktion", "Krankenhaus",
            new CommandDescription("/revive", "Belebe eine tote Person wieder"),
            new CommandDescription("/disease", "Untersuche eine Person nach Krankheiten"),
            new CommandDescription("/d", "Schreibe in den Staatschat, in dem alle Staatsfraktionen schreiben k\u00f6nnen"),
            new CommandDescription("/rezept", "Stelle einem Spieler ein Rezept aus"),
            new CommandDescription("/bloodtest", "F\u00fchre einen Bluttest durch"),
            new CommandDescription("/dropblood", "Stelle das Blut in euer Blutlager")
    )),
    NEWS("News", constructFactionInfo("News Agency", "News",
            "Die News hat eine kleine Zentrale gegen\u00fcber der Bank",
            "Licht! Kamera! Aktion! Eine spannende TV-Show erwartet uns! " +
                    "Perfekt durchgeplant von der News Agency: Die Zeitung und Ihre TV-Shows. " +
                    "Was wird uns wohl in der n\u00e4chsten Ausgabe der Zeitung erwarten?",
            "Neutrale Fraktion", "-90/70/-357",
            new CommandDescription("/zeitung", "Damit bekommst du ein Buch aus dem Equip"),
            new CommandDescription("/tvshow", "Starte eine TV-Show")
    )),
    TERRORISTS("Terroristen", constructFactionInfo("Terroristen", "Terroristen",
            "Die Terroristen haben sich in einer Lagerhalle in der N\u00e4he des Labors niedergelassen",
            "Die Terroristen k\u00f6nnen \"nur\" Bomben legen sowie Roleplay Anschl\u00e4ge machen.",
            "Neutrale Fraktion", "-55/60/494",
            new CommandDescription("/bomb", "Lege eine Bombe")
    )),
    HITMAN("Hitman", constructFactionInfo("Hitman", "Hitman",
            "Ihr HQ befindet sich in der N\u00e4he vom Luigi's",
            "Wieder einmal ein Contract! Das ist genau Ihr Ding! Sie richten Ihre Auftr\u00e4ge ab und erledigen die Contracts!",
            "Neutrale Fraktion", "303/69/63",
            new CommandDescription("/contractlist", "Lasse dir alle Contracts anzeigen"),
            new CommandDescription("/delcontract", "L\u00f6sche ein Contract")
    )),
    CHURCH("Church", constructFactionInfo("Kirche", "Kirche",
            "Die Kirche hat sich neben dem Friedhof mit Hilfe Gottes Kraft eine Kirche errichtet",
            "Hallelujah! Mit der Bibel bewaffnet halten sie Ihre Gottesdienste und verkaufen Ablassbriefe! " +
                    "Dazu bringen sie verliebte Paare zusammen und vereinen sie mit Gottes Segen!",
            "Neutrale Fraktion", "Kirche",
            new CommandDescription("/ablassbrief", "Verkaufe einen Spieler ein Ablassbrief"),
            new CommandDescription("/marry", "Verheirate 2 Spieler"),
            new CommandDescription("/gottesdienst", "Er\u00f6ffne einen Gottesdienst")
    )),
    MAFIA("Mafia", constructBadFactionInfo("La Cosa Nostra", "Cosa Nostra",
            "Sie besitzen eine gro\u00dfe Villa in der N\u00e4he des Waffenladen bzw. der Polizeistation",
            "Die Mafia besitzt die Aufgaben Drogen zu verkaufen, Waffen herzustellen und nat\u00fcrlich Roleplay zu betreiben.",
            "10/69/-464",
            new CommandDescription("/newgun", "Stelle Munition und Waffen her"),
            new CommandDescription("/sellgun", "Verkaufe deine erstellen Waffen und Munition")
    )),
    MEXICAN("Mexican", constructBadFactionInfo("Calder\u00f3n Kartell", "Calder\u00f3n",
            "Das Kartell besitzt eine Art Festung im mexikanischem Gebiet in der N\u00e4he der Fahrschule",
            "Das Kartell hat neben dem Roleplay noch die Aufgabe Drogen zu verkaufen und die beliebten Briefbomben herzustellen.",
            "-90/70/-357",
            new CommandDescription("/brief", "Hole dir mit diesem Befehl einen Brief in der Postzentrale"),
            new CommandDescription("/briefbombe", "Stelle aus einem Brief und Schwarzpulver eine Briefbombe her"))
    ),
    GANG("Gang", constructBadFactionInfo("Juggalos", "Juggalos",
            "Die Gang besitzt einen kleinen Hof umrandet mit H\u00e4usern in der N\u00e4he des Waffenladens",
            "Stra\u00dfenrennen, Drogenverkauf und Geiselnahmen! Genau das sind die Aufgaben der Gang!",
            "-160/69/201",
            new CommandDescription("/race", "Startet ein Stra\u00dfenrennen")
    )),
    KERZAKOV("Kerzakov", constructBadFactionInfo("Kerzakov Familie", "Kerzakov",
            "Die Kerzakov Basis liegt im KF Gebiet und besteht aus paar Lagerhallen",
            "Die Kerzakov kann momentan Drogen verkaufen, Geiselnahmen durchf\u00fchren und gaaaanz viel Roleplay",
            "864/68/202"
    )),
    TRIADS("Triaden", constructBadFactionInfo("Triaden", "Triaden",
            "Das Hauptquartier der Triaden befindet sich ungef\u00e4hr am Eingang von Chinatown",
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
            badFactionCommandInfo = new CommandInfo(new CommandDescription[]{
                    new CommandDescription("/selldrug", "Verkauft Drogen an einem Spieler, das Geld geht in die Fraktionsbank"),
                    new CommandDescription("/drogenbank", "Lagert oder nimmt die Drogen aus der Drogenbank aus"),
                    new CommandDescription("/bl", "L\u00e4sst dir die Spieler auf der Blackliste anzeigen"),
                    new CommandDescription("/bl add", "F\u00fcgt einen Spieler zur Blacklist hinzu"),
                    new CommandDescription("/bl remove", "Entfernt den Spieler von der Blacklist")}
            );
        }

        return constructFactionInfo(fullName, shortName, true, hqPosition, tasks, "Bad Fraktion", naviPoint,
                badFactionCommandInfo.createCopy().append(new CommandInfo(commandDescriptions)).getCommands());
    }

    private static FactionInfo constructFactionInfo(String fullName, String shortName, String hqPosition, String tasks, String factionType, String naviPoint, CommandDescription... commandDescriptions) {
        return constructFactionInfo(fullName, shortName, false, hqPosition, tasks, factionType, naviPoint, commandDescriptions);
    }

    private static FactionInfo constructFactionInfo(String fullName, String shortName, boolean badFrak, String hqPosition, String tasks, String factionType, String naviPoint, CommandDescription... commandDescriptions) {
        if (factionCommandInfo == null) {
            factionCommandInfo = new CommandInfo(new CommandDescription[]{
                    new CommandDescription("/equip", "Equippe dich"),
                    new CommandDescription("/f", "Schreibe in den Fraktionschat"),
                    new CommandDescription("/fbank", "L\u00e4sst dir den Stand der Fraktionsbank anzeigen"),
                    new CommandDescription("/fbank einzahlen", "Zahle Geld in die Fraktionsbank ein"),
                    new CommandDescription("/member", "L\u00e4sst dir alle Mitglieder deiner Fraktion anzeigen, die online sind")}
            );
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
