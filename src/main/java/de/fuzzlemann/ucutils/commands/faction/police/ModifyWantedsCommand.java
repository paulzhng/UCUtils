package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.base.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.police.Wanted;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.math.Expression;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.fuzzlemann.ucutils.base.command.CommandParam.NULL;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ModifyWantedsCommand implements TabCompletion {

    @Command(value = {"modifywanteds", "mw"}, usage = "/%label% [Spieler] [GF/SF/SSF/S/DA5/DA10/DA15/FSA/WGV oder eine Zahl]", async = true)
    public boolean onCommand(UPlayer p, String target, @CommandParam(required = false, requiredValue = NULL) Integer amount, @CommandParam(arrayStart = true, required = false) Type[] types) {
        if (amount == null && types.length == 0) return false;

        Wanted wanted = WantedManager.getWanteds(target);
        if (wanted == null) {
            TextUtils.error("Du hast /wanteds noch nicht ausgeführt.");
            return true;
        }

        String wantedReason = wanted.getReason();
        int wantedAmount = wanted.getAmount();

        for (Type flag : types) {
            if (wantedReason.contains(flag.postponeReason)) continue;

            if (flag == Type.VERY_BAD_CONDUCT) {
                if (wantedReason.contains(Type.BAD_CONDUCT.postponeReason)) {
                    wantedReason = wantedReason.replace(Type.BAD_CONDUCT.postponeReason, "");
                    wantedAmount -= 10;
                }

                if (wantedReason.contains(Type.RESISTANCE_TO_ENFORCEMENT_OFFICERS.postponeReason)) {
                    wantedReason = wantedReason.replace(Type.RESISTANCE_TO_ENFORCEMENT_OFFICERS.postponeReason, "");
                    wantedAmount -= 10;
                }
            }


            wantedReason = flag.modifyReason(wantedReason);
            wantedAmount = flag.modifyWanteds(wantedAmount);
        }

        if (amount != null) {
            if (Math.abs(amount) > 10) {
                TextUtils.error("Die Variation darf nicht größer als 10 Wanteds sein.");
                return true;
            }

            wantedAmount += amount;
        }

        if (wanted.getAmount() > wantedAmount)
            p.sendChatMessage("/clear " + target + " .");

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
        RESISTANCE_TO_ENFORCEMENT_OFFICERS("wgv", " + Widerstand gegen Vollstreckungsbeamte", "x+5");

        private final String flagArgument;
        private final String postponeReason;
        private final String wantedModification;

        Type(String flagArgument, String postponeReason, String wantedModification) {
            this.flagArgument = flagArgument;
            this.postponeReason = postponeReason;
            this.wantedModification = wantedModification;
        }

        private String modifyReason(String reason) {
            return reason + postponeReason;
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