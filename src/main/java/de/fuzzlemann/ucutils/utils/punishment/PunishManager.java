package de.fuzzlemann.ucutils.utils.punishment;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.supporter.violation.Punishment;
import de.fuzzlemann.ucutils.common.udf.data.supporter.violation.Violation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
@UDFModule(value = DataRegistry.VIOLATIONS, version = 1)
public class PunishManager implements UDFLoader<List<Violation>> {

    private static final List<Violation> VIOLATIONS = new ArrayList<>();

    public static List<String> getViolations() {
        return VIOLATIONS.stream()
                .map(Violation::getReason)
                .collect(Collectors.toList());
    }

    public static Violation getViolation(String reason) {
        return VIOLATIONS.stream()
                .filter(violation -> violation.getReason().equalsIgnoreCase(reason))
                .findFirst()
                .orElse(null);
    }

    public static Violation combineViolations(Violation[] violations) {
        ListMultimap<Punishment.PunishmentType, Punishment> punishmentMap = LinkedListMultimap.create();

        List<Punishment> disconnectPunishments = new ArrayList<>();
        for (Violation violation : violations) {
            for (Punishment punishment : violation.getPunishments()) {
                if (punishment.getType() == Punishment.PunishmentType.TEMPORARY_BAN || punishment.getType() == Punishment.PunishmentType.PERMANENT_BAN || punishment.getType() == Punishment.PunishmentType.KICK) {
                    disconnectPunishments.add(punishment);
                    continue;
                }

                punishmentMap.put(punishment.getType(), punishment);
            }
        }

        for (Punishment banPunishment : disconnectPunishments) {
            punishmentMap.put(banPunishment.getType(), banPunishment);
        }

        List<Punishment> punishments = new ArrayList<>();
        for (Punishment.PunishmentType punishmentType : punishmentMap.keySet()) {
            List<Punishment> specificPunishments = punishmentMap.get(punishmentType);

            StringBuilder sb = new StringBuilder();
            int value = 0;

            int i = 0;
            for (Punishment specificPunishment : specificPunishments) {
                if (specificPunishment.getType() == Punishment.PunishmentType.WARN) {
                    punishments.add(specificPunishment);
                    continue;
                }

                value += specificPunishment.getValue();
                sb.append(specificPunishment.getReason());

                if (++i != specificPunishments.size())
                    sb.append(" + ");
            }

            if (punishmentType != Punishment.PunishmentType.WARN)
                punishments.add(new Punishment(punishmentType, sb.toString(), value));
        }

        return new Violation(null, punishments);
    }

    @Override
    public void supply(List<Violation> violations) {
        VIOLATIONS.addAll(violations);
    }

    @Override
    public void cleanUp() {
        VIOLATIONS.clear();
    }
}