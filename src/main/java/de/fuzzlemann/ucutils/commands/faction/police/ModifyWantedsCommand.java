package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import de.fuzzlemann.ucutils.events.NameFormatEventHandler;
import de.fuzzlemann.ucutils.utils.faction.police.Wanted;
import de.fuzzlemann.ucutils.utils.math.Expression;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ModifyWantedsCommand implements TabCompletion {

    @Command(value = {"modifywanteds", "mw"}, usage = "/%label% [Spieler] [GF/SF/SSF/S/DA5/DA10/DA15/FSA/WSA/WGV]", async = true)
    public boolean onCommand(UPlayer p, String target, @CommandParam(arrayStart = true) Type[] types) {
        Wanted wanted = NameFormatEventHandler.WANTED_MAP.get(target);
        if (wanted == null) {
            TextUtils.error("Du hast /wanteds noch nicht ausgeführt.");
            return true;
        }

        String wantedReason = wanted.getReason();
        int wantedAmount = wanted.getAmount();

        for (Type flag : types) {
            if (wantedReason.contains(flag.reason)) continue;

            if (flag == Type.VERY_BAD_CONDUCT) {
                if (wantedReason.contains(Type.BAD_CONDUCT.reason)) {
                    wantedReason = wantedReason.replace(Type.BAD_CONDUCT.reason, "");
                    wantedAmount -= 10;
                }
            }

            if (flag == Type.BAD_CONDUCT) {
                if (wantedReason.contains(Type.VERY_BAD_CONDUCT.reason)) {
                    wantedReason = wantedReason.replace(Type.VERY_BAD_CONDUCT.reason, "");
                    wantedAmount -= 15;
                }
            }

            wantedReason = flag.modifyReason(wantedReason);
            wantedAmount = flag.modifyWanteds(wantedAmount);
        }

        if (wanted.getAmount() > wantedAmount)
            p.sendChatMessage("/clear " + target);

        if (wantedAmount > 69)
            wantedAmount = 69;

        if (wantedAmount == wanted.getAmount() && wantedReason.equals(wanted.getReason())) {
            TextUtils.error("Der Spieler besitzt bereits diese Modifikatoren.");
            return true;
        }

        p.sendChatMessage("/su " + wantedAmount + " " + target + " " + wantedReason);
        return true;
    }

    @Override
    public List<String> getTabCompletions(UPlayer p, String[] args) {
        if (args.length == 1) return Collections.emptyList();

        return Arrays.stream(Type.values())
                .map(t -> t.flagArgument)
                .collect(Collectors.toList());
    }

    @ParameterParser.At(TypeParser.class)
    private enum Type {
        SURRENDER("s", " + Stellung", "x-5"),
        GOOD_CONDUCT("gf", " + Gute Führung", "x-5"),
        BAD_CONDUCT("sf", " + Schlechte Führung", "x+10"),
        VERY_BAD_CONDUCT("ssf", " + Sehr schlechte Führung", "x+15"),
        DRUG_REMOVAL_5("da5", " + Drogenabnahme", "x-5"),
        DRUG_REMOVAL_10("da10", " + Drogenabnahme", "x-10"),
        DRUG_REMOVAL_15("da15", " + Drogenabnahme", "x-15"),
        DRIVERS_LICENSE_WITHDRAWAL("fsa", " + Führerscheinabnahme", "x"),
        WEAPONS_LICENSE_WITHDRAWAL("wsa", " + Waffenscheinabnahme", "x"),
        RESISTANCE_TO_ENFORCEMENT_OFFICERS("wgv", " + Widerstand gegen Vollstreckungsbeamte", "x+5");

        private final String flagArgument;
        private final String reason;
        private final String wantedModification;

        Type(String flagArgument, String reason, String wantedModification) {
            this.flagArgument = flagArgument;
            this.reason = reason;
            this.wantedModification = wantedModification;
        }

        private String modifyReason(String oldReason) {
            return oldReason + this.reason;
        }

        private int modifyWanteds(int wanteds) {
            return (int) new Expression(wantedModification.replace("x", String.valueOf(wanteds))).evaluate();
        }
    }

    public static class TypeParser implements ParameterParser<String, Type> {
        @Override
        public Type parse(String input) {
            for (Type type : Type.values()) {
                if (input.equalsIgnoreCase(type.flagArgument)) return type;
            }

            return null;
        }
    }
}