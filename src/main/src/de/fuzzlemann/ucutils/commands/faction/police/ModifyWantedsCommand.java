package de.fuzzlemann.ucutils.commands.faction.police;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.command.TabCompletion;
import de.fuzzlemann.ucutils.utils.faction.police.Wanted;
import de.fuzzlemann.ucutils.utils.faction.police.WantedManager;
import de.fuzzlemann.ucutils.utils.math.Expression;
import lombok.SneakyThrows;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ModifyWantedsCommand implements CommandExecutor, TabCompletion {

    private static Set<Type> getTypes(String[] args) {
        Set<Type> types = new HashSet<>();

        for (int i = 1; i < args.length; i++) {
            Type flag = Type.getType(args[i]);
            if (flag != null)
                types.add(flag);
        }

        return types;
    }

    @Override
    @Command(labels = {"modifywanteds", "mw"}, usage = "/%label% [Spieler] [GF/SF/S]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length < 2) return false;

        Set<Type> types = getTypes(args);
        if (types.isEmpty()) return false;

        new Thread(() -> {
            String player = args[0];

            Wanted wanted = null;
            try {
                wanted = WantedManager.getWanteds(player).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (wanted == null) return;

            String wantedReason = wanted.getReason();
            int wantedAmount = wanted.getAmount();

            for (Type flag : types) {
                wantedReason = flag.modifyReason(wantedReason);
                wantedAmount = flag.modifyWanteds(wantedAmount);
            }

            if (wanted.getAmount() > wantedAmount)
                p.sendChatMessage("/clear " + player + " .");
            p.sendChatMessage("/su " + wantedAmount + " " + player + " " + wantedReason);
        }).start();
        return true;
    }

    @Override
    public List<String> getTabCompletions(EntityPlayerSP p, String[] args) {
        if (args.length != 2) return Collections.emptyList();

        String type = args[args.length - 1].toLowerCase();
        List<String> wantedReasons = Arrays.stream(Type.values())
                .map(t -> t.flagArgument)
                .collect(Collectors.toList());

        if (type.isEmpty()) return wantedReasons;

        wantedReasons.removeIf(wantedReason -> !wantedReason.toLowerCase().startsWith(type));

        Collections.sort(wantedReasons);
        return wantedReasons;
    }

    private enum Type {
        SURRENDER("s", "", " + Stellung", "x-5"),
        GOOD("gf", "", " + Gute F\u00fchrung", "x-5"),
        BAD("sf", "", " + Schlechte F\u00fchrung", "x+5");

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

        @SneakyThrows
        private int modifyWanteds(int wanteds) {
            return (int) new Expression(wantedModification.replace("x", String.valueOf(wanteds))).evaluate();
        }
    }
}