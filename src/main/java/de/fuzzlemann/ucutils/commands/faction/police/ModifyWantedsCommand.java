package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;
import de.fuzzlemann.ucutils.utils.command.api.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.police.Wanted;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.math.Expression;
import de.fuzzlemann.ucutils.utils.text.TextUtils;
import net.minecraft.client.entity.EntityPlayerSP;
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

    @Command(value = {"modifywanteds", "mw"}, usage = "/%label% [Spieler] [GF/SF/SSF/S/DA5/DA10/DA15/FSA]", async = true)
    public boolean onCommand(EntityPlayerSP p, String target, @CommandParam(arrayStart = true) Type[] types) {
        Wanted wanted = WantedManager.getWanteds(target);
        if (wanted == null) {
            TextUtils.error("Du hast /wanteds noch nicht ausgeführt.");
            return true;
        }

        String wantedReason = wanted.getReason();
        int wantedAmount = wanted.getAmount();

        for (Type flag : types) {
            if (wantedReason.contains(flag.postponeReason)) continue;

            if (flag == Type.VERY_BAD_CONDUCT && wantedReason.contains(Type.BAD_CONDUCT.postponeReason)) {
                wantedReason = wantedReason.replace(Type.BAD_CONDUCT.postponeReason, "");
                wantedAmount -= 10;
            }

            wantedReason = flag.modifyReason(wantedReason);
            wantedAmount = flag.modifyWanteds(wantedAmount);
        }

        if (wanted.getAmount() > wantedAmount)
            p.sendChatMessage("/clear " + target + " .");

        if (wantedAmount > 69)
            wantedAmount = 69;

        p.sendChatMessage("/su " + wantedAmount + " " + target + " " + wantedReason);
        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length == 1) return Collections.emptyList();

        return Arrays.stream(Type.values())
                .map(t -> t.flagArgument)
                .collect(Collectors.toList());
    }

    @ParameterParser.At(TypeParser.class)
    private enum Type {
        SURRENDER("s", "", " + Stellung", "x-5"),
        GOOD_CONDUCT("gf", "", " + Gute Führung", "x-5"),
        BAD_CONDUCT("sf", "", " + Schlechte Führung", "x+10"),
        VERY_BAD_CONDUCT("ssf", "", " + Sehr schlechte Führung", "x+15"),
        DRUG_REMOVAL_5("da5", "", " + Drogenabnahme", "x-5"),
        DRUG_REMOVAL_10("da10", "", " + Drogenabnahme", "x-10"),
        DRUG_REMOVAL_15("da15", "", " + Drogenabnahme", "x-15"),
        DRIVERS_LICENSE_WITHDRAWAL("fsa", "", " + Führerscheinabnahme", "x");

        private final String flagArgument;
        private final String prependReason;
        private final String postponeReason;
        private final String wantedModification;

        Type(String flagArgument, String prependReason, String postponeReason, String wantedModification) {
            this.flagArgument = flagArgument;
            this.prependReason = prependReason;
            this.postponeReason = postponeReason;
            this.wantedModification = wantedModification;
        }

        static Type getType(String string) {
            for (Type flag : Type.values()) {
                if (flag.flagArgument.equalsIgnoreCase(string)) return flag;
            }

            return null;
        }

        private String modifyReason(String reason) {
            return prependReason + reason + postponeReason;
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