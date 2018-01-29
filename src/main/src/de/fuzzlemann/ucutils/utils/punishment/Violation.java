package de.fuzzlemann.ucutils.utils.punishment;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class Violation {

    private final String reason;
    private final List<Punishment> punishments;

    public Violation(String reason, Punishment... punishments) {
        List<Punishment> punishmentList = new ArrayList<>();
        Collections.addAll(punishmentList, punishments);

        this.reason = reason;
        this.punishments = punishmentList;
    }

    public Violation(String reason, List<Punishment> punishments) {
        this.reason = reason;
        this.punishments = punishments;
    }

    public String getReason() {
        return reason;
    }

    public List<String> getCommands(String player) {
        return punishments.stream()
                .map(punishment -> punishment.parseCommand(player))
                .collect(Collectors.toList());
    }

    public List<Punishment> getPunishments() {
        return punishments;
    }

    public static Violation combineViolations(List<Violation> violations) {
        ListMultimap<Punishment.PunishmentType, Punishment> punishmentMap = LinkedListMultimap.create();

        List<Punishment> banPunishments = new ArrayList<>();
        for (Violation violation : violations) {
            for (Punishment punishment : violation.getPunishments()) {
                if (punishment.getType() == Punishment.PunishmentType.TEMPORARY_BAN || punishment.getType() == Punishment.PunishmentType.PERMANENT_BAN) {
                    banPunishments.add(punishment);
                    continue;
                }

                punishmentMap.put(punishment.getType(), punishment);
            }
        }

        for (Punishment banPunishment : banPunishments) {
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
}
